package loaniq.utils;

import java.lang.reflect.Field;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


import com.google.inject.MembersInjector;

public class Log4JMembersInjector<T> implements MembersInjector<T> {
    private final Field field;
    private final Logger logger;

    Log4JMembersInjector(Field field) {
      this.field = field;
      this.logger = LogManager.getLogger(field.getDeclaringClass());
      field.setAccessible(true);
    }

    public void injectMembers(T t) {
      try {
        field.set(t, logger);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }
  }