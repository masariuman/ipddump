/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package gui;

//~--- JDK imports ------------------------------------------------------------

/**
 *
 * @author Jimmys Daskalakis
 */
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class LinkLabel extends JLabel {
    private URL       url;
    private String    text;
    private ArrayList listeners;

    //~--- constructors -------------------------------------------------------

    public LinkLabel(String textAndURL) throws MalformedURLException {
        this(textAndURL, textAndURL);
    }
    public LinkLabel(String url, String text) throws MalformedURLException {
        this(new URL(url), text);
    }
    public LinkLabel(URL url, String text) {
        setLinkURL(url);
        setLinkText(text);
        listeners=new ArrayList();
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                fireHyperlinkEvent(new HyperlinkEvent(LinkLabel.this, HyperlinkEvent.EventType.ENTERED, getLinkURL()));
            }
            public void mouseExited(MouseEvent e) {
                fireHyperlinkEvent(new HyperlinkEvent(LinkLabel.this, HyperlinkEvent.EventType.EXITED, getLinkURL()));
            }
            public void mouseClicked(MouseEvent e) {
                fireHyperlinkEvent(new HyperlinkEvent(LinkLabel.this, HyperlinkEvent.EventType.ACTIVATED,
                        getLinkURL()));
            }
        });
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param hl
     */
    public void addHyperlinkListener(HyperlinkListener hl) {
        listeners.add(hl);
    }

    /**
     * Method description
     *
     *
     * @param e
     */
    public void fireHyperlinkEvent(HyperlinkEvent e) {
        for (int i=listeners.size()-1; i>=0; i--) {
            ((HyperlinkListener) listeners.get(i)).hyperlinkUpdate(e);
        }
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return
     */
    public String getLinkText() {
        return text;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public URL getLinkURL() {
        return url;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param hl
     */
    public void removeHyperlinkListener(HyperlinkListener hl) {
        listeners.remove(hl);
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param text
     */
    public void setLinkText(String text) {
        this.text=text;
        update();
    }

    /**
     * Method description
     *
     *
     * @param url
     */
    public void setLinkURL(URL url) {
        if (url==null) {
            throw new NullPointerException("URL cannot be null");
        }

        this.url=url;
        update();
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     */
    private void update() {
        String t="<html><a href=\""+url.toExternalForm()+"\">";

        if (text!=null) {
            t+=text;
        } else {
            t+=url.toExternalForm();
        }

        t+="</a></html>";
        setText(t);
    }
}
