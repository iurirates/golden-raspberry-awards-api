package com.outsera.goldenraspberry.parser;

import java.util.Set;

public interface ProducerNameParser {
    Set<String> parse(String rawProducers);
}
