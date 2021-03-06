package org.vaadin.viritin.it;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.beanutils.BeanComparator;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.LazyList;
import org.vaadin.viritin.SortableLazyList;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;

/**
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class GridLazyLoadingAndSorting extends AbstractTest {

    @Override
    public Component getTestComponent() {

        final List<Person> orig = Service.getListOfPersons(1000);

        final MGrid<Person> g = new MGrid<Person>(
                new SortableLazyList.SortablePagingProvider<Person>() {
            @Override
            public List<Person> findEntities(int firstRow, boolean sortAscending,
                    String property) {
                List<Person> listOfPersons = new ArrayList<>(orig);
                if (property != null) {

                    Collections.sort(listOfPersons, new BeanComparator<Person>(
                            property));
                    if (!sortAscending) {
                        Collections.reverse(listOfPersons);
                    }
                }
                int last = firstRow + LazyList.DEFAULT_PAGE_SIZE;
                if (last > listOfPersons.size()) {
                    last = listOfPersons.size();
                }
                return new ArrayList<Person>(listOfPersons.subList(firstRow,
                        last));
            }
        },
                new LazyList.CountProvider() {

            @Override
            public int size() {
                return orig.size();
            }
        }
        );

        final MGrid<Person> g2 = new MGrid<Person>(Person.class);
        g2.lazyLoadFrom(
                new SortableLazyList.SortablePagingProvider<Person>() {
            @Override
            public List<Person> findEntities(int firstRow, boolean sortAscending,
                    String property) {
                List<Person> listOfPersons = new ArrayList<>(orig);
                if (property != null) {

                    Collections.sort(listOfPersons, new BeanComparator<Person>(
                            property));
                    if (!sortAscending) {
                        Collections.reverse(listOfPersons);
                    }
                }
                int last = firstRow + LazyList.DEFAULT_PAGE_SIZE;
                if (last > listOfPersons.size()) {
                    last = listOfPersons.size();
                }
                return new ArrayList<Person>(listOfPersons.subList(firstRow,
                        last));
            }
        },
                new LazyList.CountProvider() {

            @Override
            public int size() {
                return orig.size();
            }
        }
        );

        Button b = new Button("Reset loading strategy (should maintaine sort order)");
        b.addClickListener(e -> {
            g.lazyLoadFrom((int firstRow, boolean sortAscending, String property) -> {
                List<Person> listOfPersons = new ArrayList<>(orig);
                if (property != null) {
                    
                    Collections.sort(listOfPersons, new BeanComparator<Person>(
                            property));
                    if (!sortAscending) {
                        Collections.reverse(listOfPersons);
                    }
                }
                int last = firstRow + LazyList.DEFAULT_PAGE_SIZE;
                if (last > listOfPersons.size()) {
                    last = listOfPersons.size();
                }
                return new ArrayList<>(listOfPersons.subList(firstRow,
                        last));
            }, () -> orig.size());

        });

        return new MVerticalLayout(b, g, g2);
    }

}
