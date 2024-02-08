package com.dnd.namuiwiki.insfrastructure.persistence.option;

import com.dnd.namuiwiki.domain.entity.Option;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OptionRepository extends MongoRepository<Option, String> {
    Optional<Option> findByContent(Object content);
}
