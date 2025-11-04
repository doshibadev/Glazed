plugins {
    id("fabric-loom") version "1.10-SNAPSHOT"
}

base {
    archivesName = properties["archives_base_name"] as String
    version = properties["mod_version"] as String
    group = properties["maven_group"] as String
}



repositories {
    maven {
        name = "meteor-maven"
        url = uri("https://maven.meteordev.org/releases")
    }
    maven {
        name = "meteor-maven-snapshots"
        url = uri("https://maven.meteordev.org/snapshots")
    }

    maven {
        url = uri("https://jitpack.io")
    }



    dependencies {
        // Fabric
        minecraft("com.mojang:minecraft:${properties["minecraft_version"] as String}")
        mappings("net.fabricmc:yarn:${properties["yarn_mappings"] as String}:v2")
        modImplementation("net.fabricmc:fabric-loader:${properties["loader_version"] as String}")

    // Meteor
    // Use a pinned meteor_version rather than assuming a meteor artifact matching the
    // Minecraft version is published. See examples like KassuK1/BlackOut which pin
    // `meteor_version` in gradle.properties.
    modImplementation("meteordevelopment:meteor-client:${properties["meteor_version"] as String}")


        // Baritone
        modImplementation("meteordevelopment:baritone:${properties["baritone_version"] as String}-SNAPSHOT")

        implementation("com.google.code.gson:gson:2.10.1")
        



    }



    tasks {
        processResources {
            val propertyMap = mapOf(
                "version" to project.version,
                "mc_version" to project.property("minecraft_version"),
            )

            inputs.properties(propertyMap)

            filteringCharset = "UTF-8"

            filesMatching("fabric.mod.json") {
                expand(propertyMap)
            }
        }

        jar {
            val licenseSuffix = project.base.archivesName.get()
            from("LICENSE") {
                rename { "${it}_${licenseSuffix}" }
            }
        }

        java {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
        }

        withType<JavaCompile> {
            options.encoding = "UTF-8"
            options.release = 21
        }
    }
}

