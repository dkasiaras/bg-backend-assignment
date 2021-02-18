package com.dimkas.mars.marsrealestate.api

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

import spock.lang.Specification

@SpringBootTest
class UnitResourceServiceImplSpec extends Specification {

    @Autowired (required = false)
    private UnitResourceServiceImpl webController

    def "when context is loaded then all expected beans are created"() {
        expect: "the WebController is created"
            webController
    }

}
