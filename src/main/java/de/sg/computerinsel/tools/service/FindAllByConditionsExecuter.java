package de.sg.computerinsel.tools.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public class FindAllByConditionsExecuter<T> {

    @SuppressWarnings("unchecked")
    public Page<T> findByParams(final CrudRepository<T, Integer> repository, final PageRequest pagination, final String methodname,
            final Object... felder) {
        final List<Object> params = getMethodParams(felder);
        try {
            Method method;
            if (params.size() == 1) {
                final Object param1 = params.get(0);
                method = repository.getClass().getDeclaredMethod(methodname, param1.getClass(), Pageable.class);
                return (Page<T>) method.invoke(repository, param1, pagination);
            } else {
                if (params.size() == 2) {
                    final Object param1 = params.get(0);
                    final Object param2 = params.get(1);
                    method = repository.getClass().getDeclaredMethod(methodname, param1.getClass(), param2.getClass(), Pageable.class);
                    return (Page<T>) method.invoke(repository, param1, param2, pagination);
                } else {
                    final Object param1 = params.get(0);
                    final Object param2 = params.get(1);
                    final Object param3 = params.get(2);
                    method = repository.getClass().getDeclaredMethod(methodname, param1.getClass(), param2.getClass(), param3.getClass(),
                            Pageable.class);
                    return (Page<T>) method.invoke(repository, param1, param2, param3, pagination);
                }
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private List<Object> getMethodParams(final Object... felder) {
        return Arrays.asList(felder).stream().filter(f -> f instanceof String ? StringUtils.isNotBlank((String) f) : f != null)
                .collect(Collectors.toList());
    }

}