package com.exampleinyection.clase2parte2.config;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class AppConfigTest {
    @Test
    void testConfig() {
        AppConfig config = new AppConfig();
        
        config.setName("GFTApp");
        assertEquals("GFTApp", config.getName());

        config.setPassword("mySecurePassword123");
        assertEquals("mySecurePassword123", config.getPassword());
        
        AppConfig.DefaultSettings def = new AppConfig.DefaultSettings();
        def.setName("A");
        def.setAge(10);
        assertEquals("A", def.getName());
        assertEquals(10, def.getAge());
        config.setDefaults(def);
        assertEquals(def, config.getDefaults());
        AppConfig.PaginationSettings pag = new AppConfig.PaginationSettings();
        pag.setMaxSize(50);
        assertEquals(50, pag.getMaxSize());
        config.setPagination(pag);
        assertEquals(pag, config.getPagination());
        AppConfig.UpdateSettings up = new AppConfig.UpdateSettings();
        up.setDisabled(true);
        up.setMessage("No");
        assertEquals(true, up.isDisabled());
        assertEquals("No", up.getMessage());
        config.setUpdate(up);
        assertEquals(up, config.getUpdate());
    }

    @Test
    void testSpringContextHelper() {
        SpringContextHelper helper = new SpringContextHelper();
        org.springframework.context.ApplicationContext context = org.mockito.Mockito.mock(org.springframework.context.ApplicationContext.class);
        helper.setApplicationContext(context);
        org.mockito.Mockito.when(context.getBean(String.class)).thenReturn("TestBean");
        
        String result = SpringContextHelper.getBean(String.class);
        assertEquals("TestBean", result);
    }
}
