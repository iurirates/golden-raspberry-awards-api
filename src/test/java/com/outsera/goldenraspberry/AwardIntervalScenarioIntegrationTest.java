package com.outsera.goldenraspberry;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "application.csv.movies-file=classpath:movielist-scenarios.csv",

        "spring.datasource.url=jdbc:h2:mem:scenarios;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
})
class AwardIntervalScenarioIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnAllTiedProducersForMinAndMaxIntervals() throws Exception {
        mockMvc.perform(get("/api/producers/award-intervals"))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.min.length()").value(2))
                .andExpect(jsonPath("$.min[0].producer").value("Prod A"))
                .andExpect(jsonPath("$.min[0].interval").value(1))
                .andExpect(jsonPath("$.min[0].previousWin").value(2000))
                .andExpect(jsonPath("$.min[0].followingWin").value(2001))
                .andExpect(jsonPath("$.min[1].producer").value("Prod B"))
                .andExpect(jsonPath("$.min[1].interval").value(1))
                .andExpect(jsonPath("$.min[1].previousWin").value(2010))
                .andExpect(jsonPath("$.min[1].followingWin").value(2011))

                .andExpect(jsonPath("$.max.length()").value(2))
                .andExpect(jsonPath("$.max[0].producer").value("Prod C"))
                .andExpect(jsonPath("$.max[0].interval").value(10))
                .andExpect(jsonPath("$.max[0].previousWin").value(1980))
                .andExpect(jsonPath("$.max[0].followingWin").value(1990))
                .andExpect(jsonPath("$.max[1].producer").value("Prod D"))
                .andExpect(jsonPath("$.max[1].interval").value(10))
                .andExpect(jsonPath("$.max[1].previousWin").value(1995))
                .andExpect(jsonPath("$.max[1].followingWin").value(2005));
    }
}
