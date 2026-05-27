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
        "application.csv.movies-file=classpath:movielist-no-repeat.csv",

        "spring.datasource.url=jdbc:h2:mem:norepeat;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
})
class AwardIntervalEmptyScenarioIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnEmptyListsWhenNoProducerHasTwoWins() throws Exception {
        mockMvc.perform(get("/api/producers/award-intervals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.min").isArray())
                .andExpect(jsonPath("$.max").isArray())
                .andExpect(jsonPath("$.min.length()").value(0))
                .andExpect(jsonPath("$.max.length()").value(0));
    }
}
