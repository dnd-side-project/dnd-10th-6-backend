package com.dnd.namuiwiki.domain.option;

import com.dnd.namuiwiki.domain.option.entity.Option;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OptionRepository extends MongoRepository<Option, String> {
    Optional<Option> findByValue(Object value);
}
