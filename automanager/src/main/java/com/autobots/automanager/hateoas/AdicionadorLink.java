package com.autobots.automanager.hateoas;

import java.util.List;

public interface AdicionadorLink<T> {

    void adicionarLink(List<T> lista);
    void adicionarLink(T objeto);
}