package com.example.hellofx;

import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.*;


import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import javafx.collections.ObservableSet;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class MultiDatePicker extends DatePicker {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final ObservableSet<LocalDate> selectedDates = FXCollections.observableSet(new TreeSet<>());

  public MultiDatePicker() {
        super();
        setUpDatePicker();
    }

    public MultiDatePicker withRangeSelectionMode() {


        EventHandler<MouseEvent> mouseClickedEventHandler = (MouseEvent clickEvent) -> {
            if (clickEvent.getButton() == MouseButton.PRIMARY) {
                if (!this.selectedDates.contains(this.getValue())) {
                    this.selectedDates.add(this.getValue());

                    LocalDate firstDate = (LocalDate) this.selectedDates.toArray()[0];
                    LocalDate lastDate = (LocalDate) this.selectedDates.toArray()[this.selectedDates.size() - 1];
                    this.selectedDates.addAll(getRangeGaps(firstDate, lastDate));
                } else {
                    this.selectedDates.remove(this.getValue());
                    this.selectedDates.removeAll(getTailEndDatesToRemove(this.selectedDates, this.getValue()));
                    this.setValue(getClosestDateInTree(new TreeSet<>(this.selectedDates), this.getValue()));
                }
            }


      //       MultiDatePicker.this.show();

//            DatePickerContent datePickerContent = (DatePickerContent)getPopupContent();
//            datePickerContent.updateGrid();

            clickEvent.consume();
        };

        this.setDayCellFactory((DatePicker param) -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                //...
                if (item != null && !empty) {
                    //...
                    addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickedEventHandler);
                } else {
                    //...
                    removeEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickedEventHandler);
                }

                if (!selectedDates.isEmpty() && selectedDates.contains(item)) {
                    if (Objects.equals(item, selectedDates.toArray()[0]) || Objects.equals(item, selectedDates.toArray()[selectedDates.size() - 1])) {
                        setStyle("-fx-background-color: rgba(3, 169, 1, 0.7);");
                    } else {
                        setStyle("-fx-background-color: rgba(3, 169, 244, 0.7);");
                    }
                } else {
                    setStyle(null);
                }
            }
        });
        return this;
    }

    public ObservableSet<LocalDate> getSelectedDates() {
        return this.selectedDates;
    }

    private void setUpDatePicker() {
        setSkin(new DatePickerSkin(this));

        this.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return (date == null) ? "" : DATE_FORMAT.format(date);
            }

            @Override
            public LocalDate fromString(String string) {
                return ((string == null) || string.isEmpty()) ? null : LocalDate.parse(string, DATE_FORMAT);
            }
        });

        EventHandler<MouseEvent> mouseClickedEventHandler = (MouseEvent clickEvent) -> {
            if (clickEvent.getButton() == MouseButton.PRIMARY) {
                if (!this.selectedDates.contains(this.getValue())) {
                    this.selectedDates.add(this.getValue());
                } else {
                    this.selectedDates.remove(this.getValue());
                    this.setValue(getClosestDateInTree(new TreeSet<>(this.selectedDates), this.getValue()));
                }
            }

            MultiDatePicker.this.show();

            clickEvent.consume();
        };

        this.setDayCellFactory((DatePicker param) -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                //...
                if (item != null && !empty) {
                    //...
                    addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickedEventHandler);
                } else {
                    //...
                    removeEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickedEventHandler);
                }

                if (selectedDates.contains(item)) {
                    setStyle("-fx-background-color: rgba(3, 169, 244, 0.7);");
                } else {
                    setStyle(null);
                }
            }
        });
    }

    private static Set<LocalDate> getTailEndDatesToRemove(Set<LocalDate> dates, LocalDate date)
    {

        TreeSet<LocalDate> tempTree = new TreeSet<>(dates);
        tempTree.add(date);
        int higher = tempTree.tailSet(date).size();
        int lower = tempTree.headSet(date).size();

        if (lower <= higher) {
            return tempTree.headSet(date);
        } else if (lower > higher) {
            return tempTree.tailSet(date);
        } else {
            return new TreeSet<>();
        }
    }

    private static LocalDate getClosestDateInTree(TreeSet<LocalDate> dates, LocalDate date)
    {
        Long lower = null;
        Long higher = null;

        if (dates.isEmpty()) {
            return null;
        }

        if (dates.size() == 1) {
            return dates.first();
        }

        if (dates.lower(date) != null)
        {
            lower = Math.abs(DAYS.between(date, dates.lower(date)));
        }
        if (dates.higher(date) != null)
        {
            higher = Math.abs(DAYS.between(date, dates.higher(date)));
        }

        if (lower == null)
        {
            return dates.higher(date);
        } else if (higher == null)
        {
            return dates.lower(date);
        } else if (lower <= higher)
        {
            return dates.lower(date);
        } else if (lower > higher)
        {
            return dates.higher(date);
        } else
        {
            return null;
        }
    }

    private static Set<LocalDate> getRangeGaps(LocalDate min, LocalDate max)
    {
        Set<LocalDate> rangeGaps = new LinkedHashSet<>();

        if (min == null || max == null)
        {
            return rangeGaps;
        }

        LocalDate lastDate = min.plusDays(1);
        while (lastDate.isAfter(min) && lastDate.isBefore(max))
        {
            rangeGaps.add(lastDate);
            lastDate = lastDate.plusDays(1);

        }
        return rangeGaps;
    }

    public Node getPopupContent() {
        return ((DatePickerSkin)this.getSkin()).getPopupContent();
    }

}
