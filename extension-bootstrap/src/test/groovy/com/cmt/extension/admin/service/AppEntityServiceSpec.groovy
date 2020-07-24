package com.cmt.extension.admin.service

import com.cmt.extension.admin.SpiAdminApplication
import com.cmt.extension.admin.model.dto.AppDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

@SpringBootTest(classes = SpiAdminApplication)
@Transactional
@Rollback
class AppEntityServiceSpec extends Specification {
    @Autowired
    private AppService appService

    def "getAllApps"() {
        when:
            List<AppDTO> list = appService.getAllApps()
        then:
            list.size() > 0
    }

    def "addApp"() {
        when:
            Long id = appService.addApp("test-app1", 1L)
        then:
            println id
            id > 0
    }
}
