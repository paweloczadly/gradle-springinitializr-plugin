package io.oczadly.internal.config

import groovy.transform.CompileStatic

@CompileStatic
class PluginConstants {

    static final String PLUGIN_ID = 'plugin.id'

    static final String TASK_INITSPRINGBOOTPROJECT_GROUP_NAME = 'task.initSpringBootProject.group.name'
    static final String TASK_INITSPRINGBOOTPROJECT_NAME = 'task.initSpringBootProject.name'
    static final String TASK_INITSPRINGBOOTPROJECT_DESCRIPTION = 'task.initSpringBootProject.description'

    // -P parameters:
    static final String TASK_INITSPRINGBOOTPROJECT_PROPERTY_PROJECTTYPE_DEFAULT = 'task.initSpringBootProject.property.projectType.default'
    static final String TASK_INITSPRINGBOOTPROJECT_PROPERTY_LANGUAGE_DEFAULT = 'task.initSpringBootProject.property.language.default'
    static final String TASK_INITSPRINGBOOTPROJECT_PROPERTY_PROJECTNAME_DEFAULT = 'task.initSpringBootProject.property.projectName.default'
    static final String TASK_INITSPRINGBOOTPROJECT_PROPERTY_ZIPFILE_DEFAULT = 'task.initSpringBootProject.property.zipFile.default'
    static final String TASK_INITSPRINGBOOTPROJECT_PROPERTY_OUTPUTDIR_DEFAULT = 'task.initSpringBootProject.property.outputDir.default'

    // convention:
    static final String TASK_INITSPRINGBOOTPROJECT_CONVENTION_INITIALIZRURL_DEFAULT = 'task.initSpringBootProject.convention.initializrUrl.default'
    static final String TASK_INITSPRINGBOOTPROJECT_CONVENTION_METADATAENDPOINT_DEFAULT = 'task.initSpringBootProject.convention.metadataEndpoint.default'
    static final String TASK_INITSPRINGBOOTPROJECT_CONVENTION_EXTRACT_DEFAULT = 'task.initSpringBootProject.convention.extract.default'
}
