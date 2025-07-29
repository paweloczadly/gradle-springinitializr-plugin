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

    static boolean projectFilesContainText(File projectDir, String fileName, Object text) {
        String fileContent = new File(projectDir, fileName).text
        [text].flatten().every { desiredText -> fileContent.contains desiredText }
    }
}
