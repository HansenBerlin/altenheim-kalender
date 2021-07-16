package com.altenheim.kalender.CustomSerializable;
import java.io.Serializable;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class SerSimpleBoolean extends BooleanProperty implements Serializable

{

    public SerSimpleBoolean(boolean b) {
    }

    @Override
    public void bind(ObservableValue<? extends Boolean> observable) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void unbind() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isBound() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Object getBean() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addListener(ChangeListener<? super Boolean> listener) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removeListener(ChangeListener<? super Boolean> listener) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addListener(InvalidationListener listener) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean get() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void set(boolean value) {
        // TODO Auto-generated method stub
        
    }
    
}
