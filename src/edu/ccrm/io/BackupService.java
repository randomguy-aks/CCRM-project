package edu.ccrm.io;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * BackupService uses NIO.2 Path/Files and copy/move operations.
 */
public class BackupService {

    public Path backup(Path sourceExportDir, Path backupBaseDir) throws IOException {
        if (!Files.exists(sourceExportDir) || !Files.isDirectory(sourceExportDir)) {
            throw new IOException("Source export folder doesn't exist: " + sourceExportDir);
        }
        if (!Files.exists(backupBaseDir)) Files.createDirectories(backupBaseDir);
        String stamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        Path dest = backupBaseDir.resolve("backup_" + stamp);
        Files.createDirectories(dest);

        // copy files recursively
        Files.walk(sourceExportDir).forEach(src -> {
            try {
                Path rel = sourceExportDir.relativize(src);
                Path target = dest.resolve(rel);
                if (Files.isDirectory(src)) {
                    if (!Files.exists(target)) Files.createDirectories(target);
                } else {
                    Files.copy(src, target, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return dest;
    }

    public long calculateBackupSize(Path folder) throws IOException {
        final long[] total = {0L};
        Files.walk(folder).forEach(p -> {
            try {
                if (Files.isRegularFile(p)) total[0] += Files.size(p);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return total[0];
    }
}
