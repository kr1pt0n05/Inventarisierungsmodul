package com.hs_esslingen.insy.utils;

import java.util.List;

public class RelationUtils {

    private RelationUtils() {
    }

    /**
     * Verschiebt ein Item von einer alten Liste in eine neue Liste. Wird zur
     * Änderung der birelationellen Beziehungen benutzt
     *
     * @param oldList Die alte Liste, aus der das Item entfernt werden soll (darf
     *                null sein)
     * @param newList Die neue Liste, in die das Item eingefügt werden soll (darf
     *                null sein)
     * @param item    Das Objekt, das verschoben werden soll (darf nicht null sein)
     * @param <T>     Der generische Typ des Objekts
     */
    public static <T> void switchRelation(List<T> oldList, List<T> newList, T item) {

        if (item == null) {
            return;
        }
        if (oldList != null) {
            oldList.remove(item);
        }
        if (newList != null && !newList.contains(item)) {
            newList.add(item);
        }
    }
}
