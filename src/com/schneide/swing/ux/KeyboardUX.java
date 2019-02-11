package com.schneide.swing.ux;

import java.awt.AWTKeyStroke;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * UX improvements for keyboard related aspects.
 */
public final class KeyboardUX {

	private KeyboardUX() {
		super();
	}

    /**
     * Inspired by https://stackoverflow.com/a/8659116
     */
    public static void enableAltKeyMenuFor(final JFrame frame) {
    	if (!SwingUtilities.isEventDispatchThread()) {
    		SwingUtilities.invokeLater(() -> enableAltKeyMenuFor(frame));
    		return;
    	}
        final String highlightingKey = "highlight_menu"; //$NON-NLS-1$
        final JRootPane rootPane = frame.getRootPane();
        final InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ALT, 0, true), highlightingKey);
        final Action highlightMenuAction = new AbstractAction() {
            private static final long serialVersionUID = 9001583484241420215L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                final JMenuBar menuBar = rootPane.getJMenuBar();
                final JMenu menu = menuBar.getMenu(0);
                menu.doClick();
            }
        };
        rootPane.getActionMap().put(highlightingKey, highlightMenuAction);
    }

    /**
     * Inspired by https://stackoverflow.com/a/440536 and https://stackoverflow.com/a/8255423
     */
    public static void enableModernFocusTraversal() {
    	if (!SwingUtilities.isEventDispatchThread()) {
    		SwingUtilities.invokeLater(KeyboardUX::enableModernFocusTraversal);
    		return;
    	}
    	UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE); //$NON-NLS-1$
    	addLeftAndRightArrowAsFocusKeys();
    }

	private static void addLeftAndRightArrowAsFocusKeys() {
        addFocusKey(
                AWTKeyStroke.getAWTKeyStroke("RIGHT"), //$NON-NLS-1$
                KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
        addFocusKey(
                AWTKeyStroke.getAWTKeyStroke("LEFT"), //$NON-NLS-1$
                KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS);
	}

	private static void addFocusKey(final AWTKeyStroke key, final int focusTraversalId) {
	    final KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
	    final Set<AWTKeyStroke> forwardKeys = focusManager.getDefaultFocusTraversalKeys(focusTraversalId);
	    final Set<AWTKeyStroke> newForwardKeys = new HashSet<>(forwardKeys);
	    newForwardKeys.add(key);
	    focusManager.setDefaultFocusTraversalKeys(focusTraversalId, newForwardKeys);
	}
}
