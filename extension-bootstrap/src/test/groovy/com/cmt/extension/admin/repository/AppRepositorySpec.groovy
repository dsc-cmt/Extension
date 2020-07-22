package com.cmt.extension.admin.repository

import com.cmt.extension.admin.SpiAdminApplication
import com.cmt.extension.admin.model.dto.AppView
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

@SpringBootTest(classes = SpiAdminApplication)
@Transactional
@Rollback(false)
class AppRepositorySpec extends Specification {
    @Autowired
    private AppRepository appRepository

    def "findAllApps"() {
        when:
            List<AppView> list = appRepository.findAllApps()
        then:
            list.size() > 0
    }

    def "findAll"() {
        when:
            def list = appRepository.findAll()
        then:
            list.size() > 0
    }
}
