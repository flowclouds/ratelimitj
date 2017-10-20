package es.moki.ratelimij.dropwizard.filter;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.SecurityContext;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthenticatedKeyTest {
    
    private ResourceInfo resource = mock(ResourceInfo.class);

    private SecurityContext securityContext = mock(SecurityContext.class);

    @BeforeEach
    void beforeEach() throws Exception {
        doReturn(Object.class).when(resource).getResourceClass();
        when(resource.getResourceMethod()).thenReturn(Object.class.getMethod("wait"));
    }

    @DisplayName("AUTHENTICATED key should start with 'rlj' prefix")
    @Test
    void shouldStartWithPrefix() {
        when(securityContext.getUserPrincipal()).thenReturn(() -> "elliot");

        Optional<String> keyName = Key.AUTHENTICATED.create(null, resource, securityContext);

        assertThat(keyName.get()).startsWith("rlj:");
    }

    @DisplayName("AUTHENTICATED key should include user id if available")
    @Test
    void shouldIncludeUserId() {
        when(securityContext.getUserPrincipal()).thenReturn(() -> "elliot");

        Optional<String> keyName = Key.AUTHENTICATED.create(null, resource, securityContext);

        assertThat(keyName.get()).contains("usr#elliot");
    }

    @DisplayName("AUTHENTICATED key should return absent if no key available")
    @Test
    void shouldBeAbsent() {
        Optional<String> keyName = Key.AUTHENTICATED.create(null, resource, securityContext);

        assertThat(keyName).isNotPresent();
    }

}