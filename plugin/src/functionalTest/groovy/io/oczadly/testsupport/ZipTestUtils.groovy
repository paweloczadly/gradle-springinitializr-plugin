package io.oczadly.testsupport

import java.util.zip.ZipEntry
import java.util.zip.ZipFile

class ZipTestUtils {

    static boolean zipFileExistsAndNotEmpty(File zipFile) {
        zipFile.exists() && zipFile.length() > 0
    }

    static void unzipToDir(File zipFile, File targetDir) {
        targetDir.mkdirs()
        ZipFile zip = new ZipFile(zipFile)
        try {
            Enumeration<ZipEntry> entries = zip.entries()
            while (entries.hasMoreElements()) {
                ZipEntry file = entries.nextElement()
                File outFile = new File(targetDir, file.name)
                if (file.directory) {
                    outFile.mkdirs()
                } else {
                    outFile.parentFile.mkdirs()
                    outFile.withOutputStream { out ->
                        out << zip.getInputStream(file)
                    }
                }
            }
        } finally {
            zip.close()
        }
    }

    static boolean projectFilesExist(File projectDir, String... files) {
        files.every { file -> new File(projectDir, file).exists() }
    }
}
