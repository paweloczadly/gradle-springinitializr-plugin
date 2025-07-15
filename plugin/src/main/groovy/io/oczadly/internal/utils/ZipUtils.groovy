package io.oczadly.internal.utils

import java.util.zip.ZipEntry
import java.util.zip.ZipFile

class ZipUtils {

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
}
