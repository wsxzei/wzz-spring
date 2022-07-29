package cn.wzz.springframework.test.event;

import cn.wzz.springframework.context.event.ApplicationContextEvent;

public class CustomEvent extends ApplicationContextEvent {
    private Long id;
    private String message;

    public CustomEvent(Object source, Long id, String message) {
        super(source);
        this.id = id;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
