package io.oczadly.testsupport

class FilesTestUtils {

    static boolean zipFileExistsAndNotEmpty(File zipFile) {
        zipFile.exists() && zipFile.length() > 0
    }

    static boolean unzipDirectoryDoesNotExist(File unzipDir) {
        !unzipDir.exists() || unzipDir.listFiles().length == 0
    }

    static boolean projectFilesExist(File projectDir, String... files) {
        files.every { file -> new File(projectDir, file).exists() }
    }
}
