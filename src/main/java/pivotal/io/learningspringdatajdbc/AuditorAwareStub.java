package pivotal.io.learningspringdatajdbc;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

// AuditorAware implementation tells the  infrastructure who the current user or system interacting with the application is.
// The generic type T defines what type the properties annotated with @CreatedBy or @LastModifiedBy have to be.
// Normally this would be implemented using Spring Security

public class AuditorAwareStub implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("the-current-user-that-owns-this-session");
    }

}
