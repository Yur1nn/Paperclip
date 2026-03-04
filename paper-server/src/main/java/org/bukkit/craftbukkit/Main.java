package org.bukkit.craftbukkit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.util.PathConverter;

import static java.util.Arrays.asList;

public class Main {
    public static final java.time.Instant BOOT_TIME = java.time.Instant.now(); // Paper - track initial start time
    public static boolean useJline = true;
    public static boolean useConsole = true;

    // Paper start - Reset loggers after shutdown
    static {
        System.setProperty("java.util.logging.manager", "io.papermc.paper.log.CustomLogManager");
    }
    // Paper end - Reset loggers after shutdown

    public static void main(String[] args) {
        ensureDirectoryExists(new File("config"));
        ensureDirectoryExists(new File("data"));
        migrateLegacyFile(new File("bukkit.yml"), new File("config", "server.yml"));
        migrateLegacyFile(new File("commands.yml"), new File("config", "server-commands.yml"));
        migrateLegacyFile(new File("spigot.yml"), new File("config", "global-spigot.yml"));
        migrateLegacyFile(new File("paper.yml"), new File("config", "global-paper.yml"));
        migrateLegacyFile(new File("help.yml"), new File("config", "server-help.yml"));
        migrateLegacyFile(new File("permissions.yml"), new File("config", "server-permissions.yml"));
        migrateLegacyFile(new File("config", "bukkit.yml"), new File("config", "server.yml"));
        migrateLegacyFile(new File("config", "commands.yml"), new File("config", "server-commands.yml"));
        migrateLegacyFile(new File("config", "permissions.yml"), new File("config", "server-permissions.yml"));
        migrateLegacyFile(new File("config", "help.yml"), new File("config", "server-help.yml"));
        migrateLegacyFile(new File("config", "spigot.yml"), new File("config", "global-spigot.yml"));
        migrateLegacyFile(new File("config", "paper.yml"), new File("config", "global-paper.yml"));
        migrateLegacyFile(new File("config/server", "bukkit.yml"), new File("config", "server.yml"));
        migrateLegacyFile(new File("config/server", "commands.yml"), new File("config", "server-commands.yml"));
        migrateLegacyFile(new File("config/server", "permissions.yml"), new File("config", "server-permissions.yml"));
        migrateLegacyFile(new File("config/server", "help.yml"), new File("config", "server-help.yml"));
        migrateLegacyFile(new File("config/global", "spigot.yml"), new File("config", "global-spigot.yml"));
        migrateLegacyFile(new File("config/global", "paper.yml"), new File("config", "global-paper.yml"));
        migrateLegacyFile(new File("config/core", "bukkit.yml"), new File("config", "server.yml"));
        migrateLegacyFile(new File("config/core", "commands.yml"), new File("config", "server-commands.yml"));
        migrateLegacyFile(new File("config/core", "permissions.yml"), new File("config", "server-permissions.yml"));
        migrateLegacyFile(new File("config/help", "help.yml"), new File("config", "server-help.yml"));
        migrateLegacyFile(new File("config/spigot", "spigot.yml"), new File("config", "global-spigot.yml"));
        migrateLegacyFile(new File("config/paper", "paper.yml"), new File("config", "global-paper.yml"));
        migrateLegacyFile(new File("usercache.json"), new File("data", "usercache.json"));
        migrateLegacyFile(new File("version_history.json"), new File("data", "version_history.json"));
        migrateLegacyFile(new File("banned-players.json"), new File("data", "banned-players.json"));
        migrateLegacyFile(new File("banned-ips.json"), new File("data", "banned-ips.json"));
        migrateLegacyFile(new File("ops.json"), new File("data", "ops.json"));
        migrateLegacyFile(new File("whitelist.json"), new File("data", "whitelist.json"));
        if (System.getProperty("jdk.nio.maxCachedBufferSize") == null) System.setProperty("jdk.nio.maxCachedBufferSize", "262144"); // Paper - cap per-thread NIO cache size; https://www.evanjones.ca/java-bytebuffer-leak.html
        OptionParser parser = new OptionParser() {
            {
                this.acceptsAll(asList("?", "help"), "Show the help");

                this.acceptsAll(asList("c", "config"), "Properties file to use")
                        .withRequiredArg()
                        .ofType(File.class)
                        .defaultsTo(new File("server.properties"))
                        .describedAs("Properties file");

                this.acceptsAll(asList("P", "plugins"), "Plugin directory to use")
                        .withRequiredArg()
                        .ofType(File.class)
                        .defaultsTo(new File("plugins"))
                        .describedAs("Plugin directory");

                this.acceptsAll(asList("h", "host", "server-ip"), "Host to listen on")
                        .withRequiredArg()
                        .ofType(String.class)
                        .describedAs("Hostname or IP");

                this.acceptsAll(asList("W", "world-dir", "universe", "world-container"), "World container")
                        .withRequiredArg()
                        .ofType(File.class)
                        .defaultsTo(new File("."))
                        .describedAs("Directory containing worlds");

                this.acceptsAll(asList("w", "world", "level-name"), "World name")
                        .withRequiredArg()
                        .ofType(String.class)
                        .describedAs("World name");

                this.acceptsAll(asList("p", "port", "server-port"), "Port to listen on")
                        .withRequiredArg()
                        .ofType(Integer.class)
                        .describedAs("Port");

                this.accepts("serverId", "Server ID")
                        .withRequiredArg();

                this.accepts("jfrProfile", "Enable JFR profiling");

                this.accepts("pidFile", "pid File")
                        .withRequiredArg()
                        .withValuesConvertedBy(new PathConverter());

                this.acceptsAll(asList("o", "online-mode"), "Whether to use online authentication")
                        .withRequiredArg()
                        .ofType(Boolean.class)
                        .describedAs("Authentication");

                this.acceptsAll(asList("s", "size", "max-players"), "Maximum amount of players")
                        .withRequiredArg()
                        .ofType(Integer.class)
                        .describedAs("Server size");

                this.acceptsAll(asList("b", "bukkit-settings"), "File for bukkit settings")
                        .withRequiredArg()
                        .ofType(File.class)
                        .defaultsTo(new File("config", "server.yml"))
                        .describedAs("Yml file");

                this.acceptsAll(asList("C", "commands-settings"), "File for command settings")
                        .withRequiredArg()
                        .ofType(File.class)
                        .defaultsTo(new File("config", "server-commands.yml"))
                        .describedAs("Yml file");

                this.accepts("forceUpgrade", "Whether to force a world upgrade");
                this.accepts("eraseCache", "Whether to force cache erase during world upgrade");
                this.accepts("recreateRegionFiles", "Whether to recreate region files during world upgrade");
                this.accepts("safeMode", "Loads level with vanilla datapack only"); // Paper
                this.accepts("nogui", "Disables the graphical console");

                this.accepts("nojline", "Disables jline and emulates the vanilla console");

                this.accepts("noconsole", "Disables the console");

                this.acceptsAll(asList("v", "version"), "Show the CraftBukkit Version");

                this.accepts("demo", "Demo mode");

                this.accepts("bonusChest", "Enable the bonus chest");

                this.accepts("initSettings", "Only create configuration files and then exit"); // SPIGOT-5761: Add initSettings option

                this.acceptsAll(asList("S", "spigot-settings"), "File for spigot settings")
                        .withRequiredArg()
                        .ofType(File.class)
                        .defaultsTo(new File("config", "global-spigot.yml"))
                        .describedAs("Yml file");

                this.acceptsAll(asList("paper-dir", "paper-settings-directory"), "Directory for Paper settings")
                        .withRequiredArg()
                        .ofType(File.class)
                        .defaultsTo(new File(io.papermc.paper.configuration.PaperConfigurations.CONFIG_DIR))
                        .describedAs("Config directory");

                this.acceptsAll(asList("paper", "paper-settings"), "File for Paper settings")
                        .withRequiredArg()
                        .ofType(File.class)
                        .defaultsTo(new File("config", "global-paper.yml"))
                        .describedAs("Yml file");

                this.acceptsAll(asList("add-plugin", "add-extra-plugin-jar"), "Specify paths to extra plugin jars to be loaded in addition to those in the plugins folder. This argument can be specified multiple times, once for each extra plugin jar path.")
                        .withRequiredArg()
                        .ofType(File.class)
                        .defaultsTo(new File[] {})
                        .describedAs("Jar file");

                this.acceptsAll(asList("add-plugin-dir", "add-extra-plugin-dir"), "Specify paths to extra plugin directories to be loaded in addition to the plugins folder. This argument can be specified multiple times, once for each extra plugin dir path.")
                        .withRequiredArg()
                        .ofType(File.class)
                        .defaultsTo(new File[] {})
                        .describedAs("Plugin directory");

                this.accepts("server-name", "Name of the server")
                        .withRequiredArg()
                        .ofType(String.class)
                        .defaultsTo("Unknown Server")
                        .describedAs("Name");
            }
        };

        OptionSet options = null;

        try {
            options = parser.parse(args);
        } catch (joptsimple.OptionException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage());
        }

        if ((options == null) || (options.has("?"))) {
            try {
                parser.printHelpOn(System.out);
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (options.has("v")) {
            System.out.println(CraftServer.class.getPackage().getImplementationVersion());
        } else {
            // Do you love Java using + and ! as string based identifiers? I sure do!
            String path = new File(".").getAbsolutePath();
            if (path.contains("!") || path.contains("+")) {
                System.err.println("Cannot run server in a directory with ! or + in the pathname. Please rename the affected folders and try again.");
                return;
            }

            // Paper start - Improve java version check
            boolean skip = Boolean.getBoolean("Paper.IgnoreJavaVersion");
            String javaVersionName = System.getProperty("java.version");
            // J2SE SDK/JRE Version String Naming Convention
            boolean isPreRelease = javaVersionName.contains("-");
            if (isPreRelease) {
                if (!skip) {
                    System.err.println("Unsupported Java detected (" + javaVersionName + "). You are running an unsupported, non official, version. Only general availability versions of Java are supported. Please update your Java version. See https://docs.papermc.io/paper/faq#unsupported-java-detected-what-do-i-do for more information.");
                    return;
                }

                System.err.println("Unsupported Java detected ("+ javaVersionName + "), but the check was skipped. Proceed with caution! ");
            }
            // Paper end - Improve java version check

            try {
                if (options.has("nojline")) {
                    System.setProperty(net.minecrell.terminalconsole.TerminalConsoleAppender.JLINE_OVERRIDE_PROPERTY, "false");
                    useJline = false;
                }

                if (options.has("noconsole")) {
                    Main.useConsole = false;
                    useJline = false; // Paper
                    System.setProperty(net.minecrell.terminalconsole.TerminalConsoleAppender.JLINE_OVERRIDE_PROPERTY, "false"); // Paper
                }

                System.setProperty("library.jansi.version", "Paper"); // Paper - set meaningless jansi version to prevent git builds from crashing on Windows
                System.setProperty("jdk.console", "java.base"); // Paper - revert default console provider back to java.base so we can have our own jline

                io.papermc.paper.PaperBootstrap.boot(options);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    private static void ensureDirectoryExists(final File directory) {
        if (directory.exists() || directory.mkdirs()) {
            return;
        }
        throw new IllegalStateException("Unable to create required directory: " + directory.getAbsolutePath());
    }

    private static void migrateLegacyFile(final File legacyFile, final File targetFile) {
        if (!legacyFile.exists() || targetFile.exists()) {
            return;
        }
        final File parent = targetFile.getParentFile();
        if (parent != null) {
            ensureDirectoryExists(parent);
        }
        try {
            Files.move(legacyFile.toPath(), targetFile.toPath());
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to migrate " + legacyFile.getAbsolutePath() + " to " + targetFile.getAbsolutePath(), ex);
        }
    }
}
