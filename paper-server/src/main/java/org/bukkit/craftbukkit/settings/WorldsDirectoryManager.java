package org.bukkit.craftbukkit.settings;

import java.io.File;
import java.util.Objects;

public final class WorldsDirectoryManager {

    private WorldsDirectoryManager() {
    }

    public static File resolveWorldsContainer(final File serverRoot) {
        final File normalizedServerRoot = Objects.requireNonNull(serverRoot, "serverRoot");
        final File worldsContainer = new File(normalizedServerRoot, "worlds");
        if (worldsContainer.exists() && !worldsContainer.isDirectory()) {
            throw new IllegalStateException("Worlds container path exists but is not a directory: " + worldsContainer.getAbsolutePath());
        }
        if (!worldsContainer.exists() && !worldsContainer.mkdirs()) {
            throw new IllegalStateException("Unable to create worlds container directory: " + worldsContainer.getAbsolutePath());
        }
        /*
         * Example layout:
         * <server-root>/
         *   worlds/
         *     world/
         *     world_nether/
         *     world_the_end/
         */
        // TODO: Read worlds container name/path from fork config (default: "worlds").
        return worldsContainer;
    }
}
