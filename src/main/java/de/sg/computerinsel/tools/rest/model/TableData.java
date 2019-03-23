package de.sg.computerinsel.tools.rest.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.NullHandling;
import org.springframework.data.domain.Sort.Order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableData {

    private static final String DEFAULT_SORTORDER = "asc";

    private static final String DEFAULT_SORT = "id";

    private static final int DEFAULT_SIZE = 10;

    private static final int DEFAULT_PAGE = 0;

    private int size;

    private int page;

    private String sort;

    private String sortorder;

    public PageRequest getPagination() {
        final int sizeOrDefault = this.size == 0 ? DEFAULT_SIZE : this.size;
        final int pageOrDefault = this.page < 0 ? DEFAULT_PAGE : this.page;
        final String sortOrDefault = StringUtils.isBlank(this.sort) ? DEFAULT_SORT : this.sort;
        final String sortorderOrDefault = StringUtils.isBlank(this.sortorder) ? DEFAULT_SORTORDER : this.sortorder;
        return getPagination(sizeOrDefault, pageOrDefault, sortorderOrDefault, sortOrDefault);
    }

    private PageRequest getPagination(final int size, final int page, final String sortorder, final String sort) {
        final Direction direction = StringUtils.equalsIgnoreCase("DESC", sortorder) ? Sort.Direction.DESC : Sort.Direction.ASC;

        final List<Order> order = new ArrayList<>();
        for (final String s : sort.split(",")) {
            Order o = direction == Sort.Direction.DESC ? Order.desc(s) : Order.asc(s);
            // HSQLDB unterstützt dies nicht
            o = o.with(NullHandling.NULLS_LAST);
            order.add(o);
        }
        return PageRequest.of(page, size, Sort.by(order));
    }

}
