package com.araia.henock.volve.Objects;

import java.io.Serializable;

public class ModelLogin implements Serializable {

    private String message;

    private Token token=new Token();

    private String status;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public Token getToken ()
    {
        return token;
    }

    public void setToken (Token token)
    {
        this.token = token;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }
}