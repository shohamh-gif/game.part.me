package org.example;

public class InstructionsPanel extends BackgroundPanel {

    public InstructionsPanel(int width, int height) {
        // אנחנו קוראים לבנאי של BackgroundPanel עם התמונה של ההוראות
        super("/background_instructions.png"); // או תמונה אחרת אם יש לך
        this.setBounds(0, 0, width, height);
    }
}