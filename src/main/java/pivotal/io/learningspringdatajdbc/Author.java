package pivotal.io.learningspringdatajdbc;

import org.springframework.data.annotation.Id;

public class Author {
    @Id Long id;
    String name;
}
