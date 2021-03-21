package com.stefata.sofiasupermarketsapi.scheduled

import com.ninjasquad.springmockk.MockkBean
import com.stefata.sofiasupermarketsapi.flows.*
import io.mockk.justRun
import io.mockk.verifyAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [FlowsConfig::class, ScheduledFlowsRunner::class])
internal class ScheduledFlowsRunnerTest {

    @MockkBean
    lateinit var billaFlow: BillaFlow

    @MockkBean
    lateinit var kauflandFlow: KauflandFlow

    @MockkBean
    lateinit var lidlFlow: LidlFlow

    @MockkBean
    lateinit var fantasticoFlow: FantasticoFlow

    @Autowired
    lateinit var underTest: ScheduledFlowsRunner

    @Test
    fun `test running flows`() {

        justRun { billaFlow.runSafely() }
        justRun { kauflandFlow.runSafely() }
        justRun { lidlFlow.runSafely() }
        justRun { fantasticoFlow.runSafely() }

        underTest.runFlows()

        verifyAll {
            billaFlow.runSafely()
            kauflandFlow.runSafely()
            lidlFlow.runSafely()
            fantasticoFlow.runSafely()
        }

    }

}