package omok.util;

/**
 * The `ButtonClickListener` interface defines a contract for classes that listen for button click events.
 * Implementing classes should provide the `onButtonClick` method to handle button clicks.
 */
public interface ButtonClickListener {

    /**
     * Handles a button click event by providing the button's row and column indices.
     *
     * @param i The row index of the clicked button.
     * @param j The column index of the clicked button.
     */
    void onButtonClick(int i, int j);
}
