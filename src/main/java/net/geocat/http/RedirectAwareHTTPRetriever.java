/*
 *  =============================================================================
 *  ===  Copyright (C) 2021 Food and Agriculture Organization of the
 *  ===  United Nations (FAO-UN), United Nations World Food Programme (WFP)
 *  ===  and United Nations Environment Programme (UNEP)
 *  ===
 *  ===  This program is free software; you can redistribute it and/or modify
 *  ===  it under the terms of the GNU General Public License as published by
 *  ===  the Free Software Foundation; either version 2 of the License, or (at
 *  ===  your option) any later version.
 *  ===
 *  ===  This program is distributed in the hope that it will be useful, but
 *  ===  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  ===  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  ===  General Public License for more details.
 *  ===
 *  ===  You should have received a copy of the GNU General Public License
 *  ===  along with this program; if not, write to the Free Software
 *  ===  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 *  ===
 *  ===  Contact: Jeroen Ticheler - FAO - Viale delle Terme di Caracalla 2,
 *  ===  Rome - Italy. email: geonetwork@osgeo.org
 *  ===
 *  ===  Development of this program was financed by the European Union within
 *  ===  Service Contract NUMBER – 941143 – IPR – 2021 with subject matter
 *  ===  "Facilitating a sustainable evolution and maintenance of the INSPIRE
 *  ===  Geoportal", performed in the period 2021-2023.
 *  ===
 *  ===  Contact: JRC Unit B.6 Digital Economy, Via Enrico Fermi 2749,
 *  ===  21027 Ispra, Italy. email: JRC-INSPIRE-SUPPORT@ec.europa.eu
 *  ==============================================================================
 */

package net.geocat.http;

import net.geocat.database.linkchecker.entities.helper.HttpResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Scope("prototype")
@Qualifier("redirectAwareHTTPRetriever")
public class RedirectAwareHTTPRetriever implements IHTTPRetriever {

    public static int MAXREDIRECTS = 5;
    @Autowired
    @Qualifier("basicHTTPRetriever")
    public BasicHTTPRetriever retriever; // public for testing
    Logger logger = LoggerFactory.getLogger(RedirectAwareHTTPRetriever.class);

    public RedirectAwareHTTPRetriever() {

    }


    @Override
    public HttpResult retrieveXML(String verb, String location, String body, String cookie, IContinueReadingPredicate predicate)
            throws IOException, SecurityException, ExceptionWithCookies, RedirectException {
        return _retrieveXML(verb, location, body, cookie, MAXREDIRECTS, predicate);
    }


    protected HttpResult _retrieveXML(String verb, String location, String body, String cookie, int nRedirectsRemaining, IContinueReadingPredicate predicate)
            throws IOException, SecurityException, ExceptionWithCookies, RedirectException {
        try {
            return retriever.retrieveXML(verb, location, body, cookie, predicate);
        } catch (RedirectException re) {
            if (nRedirectsRemaining <= 0)
                throw new IOException("too many redirects!");
            logger.debug("     REDIRECTED TO location=" + re.getNewLocation());
            return _retrieveXML(verb, re.getNewLocation(), body, cookie, nRedirectsRemaining--, predicate);
        }
    }
}
