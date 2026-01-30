# Maven Publish 配置示例

## 方案一：GitHub Packages（推荐）

### 1. 在 build.gradle 中添加发布插件

```gradle
plugins {
    id 'com.android.library'
    id 'maven-publish'
}

version = "2.4.0"
group = "com.github.fanofdemo"  // 使用你的 GitHub 用户名
```

### 2. 配置发布信息

```gradle
afterEvaluate {
    publishing {
        publications {
            release(MavenPublication) {
                from components.release
                groupId = 'com.github.fanofdemo'
                artifactId = 'FloatMenu'
                version = '2.4.0'

                pom {
                    name = 'FloatMenu'
                    description = 'A lightweight and easy-to-use Android floating menu library'
                    url = 'https://github.com/fanOfDemo/FloatMenuSample'

                    licenses {
                        license {
                            name = 'The BSD 3-Clause License'
                            url = 'http://www.opensource.org/licenses/BSD-3-Clause'
                        }
                    }

                    developers {
                        developer {
                            id = 'coldbrando'
                            name = 'ColdBrando'
                            email = 'coderbrando@gmail.com'
                        }
                    }

                    scm {
                        connection = 'scm:git:github.com/fanOfDemo/FloatMenuSample.git'
                        developerConnection = 'scm:git:ssh://github.com/fanOfDemo/FloatMenuSample.git'
                        url = 'https://github.com/fanOfDemo/FloatMenuSample/tree/master'
                    }
                }
            }
        }

        repositories {
            maven {
                name = "GitHubPackages"
                url = "https://maven.pkg.github.com/fanOfDemo/FloatMenuSample"
                credentials {
                    username = System.getenv("GITHUB_USER") || project.findProperty("gpr.user") ?: System.getenv("USERNAME")
                    password = System.getenv("GITHUB_TOKEN") || project.findProperty("gpr.key") ?: System.getenv("TOKEN")
                }
            }
        }
    }
}
```

### 3. 在 local.properties 中添加凭据

```properties
gpr.user=你的GitHub用户名
gpr.key=你的GitHub Token (需要有 repo 和 write:packages 权限)
```

### 4. 发布到 GitHub Packages

```bash
./gradlew publish
```

### 5. 使用方式

在项目的 `settings.gradle` 中添加：

```gradle
dependencyResolutionManagement {
    repositories {
        maven {
            url = "https://maven.pkg.github.com/fanOfDemo/FloatMenuSample"
            credentials {
                username = "你的GitHub用户名"
                password = "你的GitHub Token"
            }
        }
    }
}
```

或使用公开访问（GitHub Packages 支持公开包）：

```gradle
repositories {
    maven {
        url = "https://maven.pkg.github.com/fanOfDemo/FloatMenuSample"
    }
}
```

---

## 方案二：Maven Central（通过 Sonatype）

### 1. 注册 Sonatype JIRA 账号

访问：https://issues.sonatype.org/

### 2. 创建新项目票据

- Project: Community Support - New Project
- Summary: FloatMenu Project Repository
- Group ID: com.github.fanofdemo（需要证明你拥有该 GitHub 组织）
- 或者使用 io.github.coldbrando（自动验证）

### 3. 等待审核（通常 1-2 个工作日）

### 4. 配置 Gradle

```gradle
plugins {
    id 'com.android.library'
    id 'signing'
    id 'maven-publish'
}

version = "2.4.0"
group = "io.github.coldbrando"  // 或你申请的 groupId

android {
    // ... 现有配置
}

ext {
    PUBLISH_GROUP_ID = group
    PUBLISH_ARTIFACT_ID = 'floatmenu'
    PUBLISH_VERSION = version
}

ext["signing.keyId"] = ""
ext["signing.password"] = ""
ext["signing.secretKeyRingFile"] = ""
ext["ossrhUsername"] = ""
ext["ossrhPassword"] = ""

File secretPropsFile = project.rootProject.file('local.properties')
if (secretPropsFile.exists()) {
    Properties p = new Properties()
    p.load(new FileInputStream(secretPropsFile))
    p.each { name, value ->
        ext[name] = value
    }
}

publishing {
    publications {
        release(MavenPublication) {
            groupId = PUBLISH_GROUP_ID
            artifactId = PUBLISH_ARTIFACT_ID
            version = PUBLISH_VERSION

            artifact("$buildDir/outputs/aar/FloatMenu-release.aar")

            pom {
                name = PUBLISH_ARTIFACT_ID
                description = 'A lightweight and easy-to-use Android floating menu library'
                url = 'https://github.com/fanOfDemo/FloatMenuSample'

                licenses {
                    license {
                        name = 'The BSD 3-Clause License'
                        url = 'https://opensource.org/licenses/BSD-3-Clause'
                    }
                }

                developers {
                    developer {
                        id = 'coldbrando'
                        name = 'ColdBrando'
                        email = 'coderbrando@gmail.com'
                    }
                }

                scm {
                    connection = 'scm:git:github.com/fanOfDemo/FloatMenuSample.git'
                    developerConnection = 'scm:git:ssh://github.com/fanOfDemo/FloatMenuSample.git'
                    url = 'https://github.com/fanOfDemo/FloatMenuSample/tree/master'
                }
            }

            pom.withXml {
                def dependenciesNode = asNode().appendNode('dependencies')
                configurations.implementation.allDependencies.each {
                    if (it.name != 'unspecified') {
                        def dependencyNode = dependenciesNode.appendNode('dependency')
                        dependencyNode.appendNode('groupId', it.group)
                        dependencyNode.appendNode('artifactId', it.name)
                        dependencyNode.appendNode('version', it.version)
                    }
                }
            }
        }
    }

    repositories {
        maven {
            name = "SonatypeStaging"
            url = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
            credentials {
                username ossrhUsername
                password ossrhPassword
            }
        }
    }
}

signing {
    sign publishing.publications.release
}
```

### 5. 配置 local.properties

```properties
signing.keyId=你的密钥ID
signing.password=你的密钥密码
signing.secretKeyRingFile=~/.gnupg/secring.gpg
ossrhUsername=你的Sonatype用户名
ossrhPassword=你的Sonatype密码
```

### 6. 生成 GPG 密钥

```bash
# 安装 GPG 工具
# macOS: brew install gnupg
# Windows: 下载 Gpg4win

# 生成密钥
gpg --gen-key

# 列出密钥
gpg --list-keys

# 导出公钥
gpg --keyserver keyserver.ubuntu.com --send-keys 你的密钥ID

# 导出私钥环
gpg --export-secret-keys > ~/.gnupg/secring.gpg
```

### 7. 发布

```bash
# 构建并上传到 Sonatype
./gradlew publishReleasePublicationToSonatypeStagingRepository

# 登录 Sonatype Nexus 检查并发布
# https://oss.sonatype.org/#stagingRepositories
```

---

## 方案三：JitPack（最简单）

### 1. 无需任何配置

只需确保项目在 GitHub 上有正确的 Gradle 结构

### 2. 使用方式

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.fanOfDemo:FloatMenuSample:v2.4.0'
}
```

### 3. 发布流程

JitPack 会自动从 GitHub 构建：
1. Push tag 到 GitHub
2. 用户引用时，JitPack 自动构建
3. 无需手动发布

**注意：** 需要确保项目根目录有正确的 build.gradle

---

## 方案对比

| 方案 | 难度 | 速度 | 稳定性 | 推荐度 |
|:---|:---:|:---:|:---:|:---:|
| GitHub Packages | ⭐⭐ | 快 | 高 | ⭐⭐⭐⭐⭐ |
| JitPack | ⭐ | 快 | 中 | ⭐⭐⭐⭐ |
| Maven Central | ⭐⭐⭐⭐⭐ | 慢 | 最高 | ⭐⭐⭐ |

---

## 我的建议

**对于你的项目，我推荐使用 GitHub Packages：**

1. 你已经在使用 GitHub
2. 配置相对简单
3. 可以配合 GitHub Actions 自动化
4. 免费且稳定

需要我帮你配置其中任何一个方案吗？
