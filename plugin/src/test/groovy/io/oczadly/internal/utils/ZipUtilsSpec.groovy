package io.oczadly.internal.utils

import spock.lang.Specification

class ZipUtilsSpec extends Specification {

    def 'uzipToDir should extract files from a zip archive to the specified directory'() {
        given:
        File zipFile = new File('src/test/resources/test.zip')
        File targetDir = new File('build/test-unzip')
        targetDir.deleteDir() // Ensure clean state

        when:
        ZipUtils.unzipToDir zipFile, targetDir

        then:
        targetDir.exists()
        new File(targetDir, 'file1.txt').exists()
        new File(targetDir, 'subdir/file2.txt').exists()
    }
}
