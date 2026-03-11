# my-spring-plugin

A Gradle plugin for Spring Boot projects that scaffolds boilerplate classes from the command line.

---

## Usage

This plugin is intended to be used locally via a composite build. In your Spring Boot project's `settings.gradle.kts`:

```kotlin
includeBuild("../my-spring-plugin")
```

Then apply it in `build.gradle.kts`:

```kotlin
plugins {
    id("com.salumsu.my-spring-plugin")
}
```

---

## Configuration

```kotlin
mySpringPlugin {
    basePackage = "com.example.myapp"   // Required

    // Optional — override default subpackage paths
    entityPath     = "entity"           // default: "entity"
    servicePath    = "services"         // default: "services"
    repositoryPath = "repositories"     // default: "repositories"
    controllerPath = "controllers"      // default: "controllers"
}
```

> `basePackage` is required. The plugin throws a `GradleException` at evaluation time if it is missing.

---

## Tasks

All tasks share these common options:

| Option | Description | Required |
|--------|-------------|----------|
| `--path` | Overrides the default subpackage path for the generated file | No |
| `--overwrite` | Overwrite the file if it already exists | No |

---

### `makeEntity`

Scaffolds a JPA entity class.

```bash
./gradlew makeEntity --name=User
./gradlew makeEntity --name=User --lombok=true
./gradlew makeEntity --name=User --path=domain.model
```

| Option | Description | Required |
|--------|-------------|----------|
| `--name` | Name of the entity class | Yes |
| `--lombok` | Add Lombok annotations | No |

---

### `makeRepository`

Scaffolds a Spring Data repository interface.

```bash
./gradlew makeRepository --name=User --model=entity.User
./gradlew makeRepository --name=User --model=entity.User --type=String
```

| Option | Description | Required |
|--------|-------------|----------|
| `--name` | Name of the repository interface | Yes |
| `--model` | Class path of the model from base package | Yes |
| `--type` | Primary key type of the model (default: `Long`) | No |

---

### `makeService`

Scaffolds a service class.

```bash
./gradlew makeService --name=User
./gradlew makeService --name=User --lombok=true --repository=repositories.UserRepository
```

| Option | Description | Required |
|--------|-------------|----------|
| `--name` | Name of the service class | Yes |
| `--lombok` | Add Lombok annotations | No |
| `--repository` | Class path of the repository interface from base package | No |

---

### `makeController`

Scaffolds a REST controller class.

```bash
./gradlew makeController --name=User
./gradlew makeController --name=User --lombok=true --service=services.UserService --endpoint=/users
```

| Option | Description | Required |
|--------|-------------|----------|
| `--name` | Name of the controller class | Yes |
| `--lombok` | Add Lombok annotations | No |
| `--service` | Class path of the service class from base package | No |
| `--endpoint` | Base endpoint for the controller | No |

---

## Example

Given `basePackage = "com.example.myapp"`:

```bash
./gradlew makeEntity --name=User --lombok=true
./gradlew makeRepository --name=UserRepository --model=entity.User
./gradlew makeService --name=UserService --lombok=true --repository=repositories.UserRepository
./gradlew makeController --name=UserController --lombok=true --service=services.UserService --endpoint=/users
```

Generates:

```
src/main/java/com/example/myapp/entity/User.java
src/main/java/com/example/myapp/repositories/UserRepository.java
src/main/java/com/example/myapp/services/UserService.java
src/main/java/com/example/myapp/controllers/UserController.java
```

---

## License

MIT
