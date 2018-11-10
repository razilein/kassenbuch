package de.sg.computerinsel.tools.rest.model;

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
        final int size = this.size == 0 ? DEFAULT_SIZE : this.size;
        final int page = this.page < 0 ? DEFAULT_PAGE : this.page;
        final String sort = StringUtils.isBlank(this.sort) ? DEFAULT_SORT : this.sort;
        final String sortorder = StringUtils.isBlank(this.sortorder) ? DEFAULT_SORTORDER : this.sortorder;
        return getPagination(size, page, sortorder, sort);
    }

    private PageRequest getPagination(final int size, final int page, final String sortorder, final String sort) {
        final Direction direction = StringUtils.equalsIgnoreCase("DESC", sortorder) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Order order = direction == Sort.Direction.DESC ? Order.desc(sort) : Order.asc(sort);
        // HSQLDB unterstützt dies nicht
        order = order.with(NullHandling.NULLS_LAST);
        return PageRequest.of(page, size, Sort.by(order));
    }

}
