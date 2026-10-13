/*
 * Copyright (c) 2002-2014, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.searchstats.web;

import fr.paris.lutece.plugins.searchstats.business.QueryRecord;
import fr.paris.lutece.plugins.searchstats.business.QueryRecordHome;
import fr.paris.lutece.plugins.searchstats.business.RecordCount;
import fr.paris.lutece.plugins.searchstats.business.RecordFilter;
import fr.paris.lutece.plugins.searchstats.service.TermRecord;
import fr.paris.lutece.plugins.searchstats.service.TermRecordService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.cdi.mvc.Models;
import fr.paris.lutece.portal.web.util.IPager;
import fr.paris.lutece.portal.web.util.Pager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

/**
 * This class provides the user interface to browse the search statistics
 */
@SessionScoped
@Named
@Controller( controllerJsp = SearchStatsJspBean.CONTROLLER_JSP, controllerPath = SearchStatsJspBean.CONTROLLER_PATH, right = SearchStatsJspBean.RIGHT_MANAGE_SEARCH_STATS, securityTokenEnabled = true )
public class SearchStatsJspBean extends MVCAdminJspBean
{
    /** The right of the feature */
    public static final String RIGHT_MANAGE_SEARCH_STATS = "SEARCH_STATS_MANAGEMENT";
    /** The controller JSP */
    public static final String CONTROLLER_JSP = "ManageSearchStats.jsp";
    /** The controller path */
    public static final String CONTROLLER_PATH = "jsp/admin/plugins/searchstats/";

    private static final long serialVersionUID = -3071874519256120713L;

    // Templates
    private static final String TEMPLATE_MANAGE_SEARCH_STATS = "admin/plugins/searchstats/manage_searchstats.html";

    // Properties
    private static final String PROPERTY_PAGE_TITLE_MANAGE_SEARCH_STATS = "searchstats.manage_searchstats.pageTitle";
    private static final String PROPERTY_ITEMS_PER_PAGE = "searchstats.listDates.itemsPerPage";
    private static final String PROPERTY_FORMAT_YEAR = "searchstats.format.year";
    private static final String PROPERTY_FORMAT_MONTH = "searchstats.format.month";
    private static final String PROPERTY_FORMAT_DAY = "searchstats.format.day";

    // Views
    private static final String VIEW_MANAGE_SEARCH_STATS = "manageSearchStats";

    // Actions
    private static final String ACTION_REFRESH = "refresh";

    // Marks
    private static final String MARK_DATES = "records_list";
    private static final String MARK_QUERIES = "queries_list";
    private static final String MARK_TERMS = "terms_list";
    private static final String MARK_NO_RESULT_STATE = "no_result_state";
    private static final String MARK_DISPLAY_DETAILS = "display_details";
    private static final String MARK_DETAIL_DATE = "detail_date";

    // Parameters
    private static final String PARAMETER_YEAR = "year";
    private static final String PARAMETER_MONTH = "month";
    private static final String PARAMETER_DAY = "day";
    private static final String PARAMETER_NO_RESULT = "no_result";

    private static final int MAX_MONTH = 12;
    private static final int MAX_DAY = 31;

    @Inject
    @Pager( name = "searchstats.dates", listBookmark = MARK_DATES, defaultItemsPerPage = PROPERTY_ITEMS_PER_PAGE, baseUrl = CONTROLLER_PATH + CONTROLLER_JSP )
    private IPager<RecordCount, Void> _pagerDates;

    private boolean _bShowOnlyNoResultQueries;

    /**
     * Displays the dates having search queries and, when a date is given, the queries and the most searched terms of that date.
     *
     * @param request
     *            the HTTP request
     * @param model
     *            the model
     * @return the page
     */
    @View( value = VIEW_MANAGE_SEARCH_STATS, defaultView = true, securityTokenAction = ACTION_REFRESH )
    public String getManageSearchStats( HttpServletRequest request, Models model )
    {
        int nYear = getDateParameter( request, PARAMETER_YEAR, Integer.MAX_VALUE );
        int nMonth = ( nYear != RecordFilter.NOT_DEFINED ) ? getDateParameter( request, PARAMETER_MONTH, MAX_MONTH ) : RecordFilter.NOT_DEFINED;
        int nDay = ( nMonth != RecordFilter.NOT_DEFINED ) ? getDateParameter( request, PARAMETER_DAY, MAX_DAY ) : RecordFilter.NOT_DEFINED;
        boolean bDisplayDetails = nYear != RecordFilter.NOT_DEFINED;

        RecordFilter filter = new RecordFilter( );
        filter.setNoResults( _bShowOnlyNoResultQueries );
        filter.setYear( nYear );
        filter.setMonth( nMonth );
        filter.setDay( nDay );

        List<QueryRecord> listQueries = bDisplayDetails ? QueryRecordHome.findQueryRecordsListByCriteria( getPlugin( ), filter ) : new ArrayList<>( );
        List<TermRecord> listTopTerms = TermRecordService.getTopTerms( listQueries );

        _pagerDates.withListItem( QueryRecordHome.findRecordsDatesList( getPlugin( ), _bShowOnlyNoResultQueries ) ).populateModels( request, model, getLocale( ) );

        model.put( MARK_DISPLAY_DETAILS, bDisplayDetails );
        model.put( MARK_DETAIL_DATE, getDetailDateLabel( nYear, nMonth, nDay ) );
        model.put( MARK_QUERIES, listQueries );
        model.put( MARK_TERMS, listTopTerms );
        model.put( MARK_NO_RESULT_STATE, _bShowOnlyNoResultQueries );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_SEARCH_STATS, TEMPLATE_MANAGE_SEARCH_STATS, model );
    }

    /**
     * Sets whether only the queries without result are displayed.
     *
     * @param request
     *            the HTTP request
     * @return the URL of the stats view
     */
    @Action( ACTION_REFRESH )
    public String doRefresh( HttpServletRequest request )
    {
        _bShowOnlyNoResultQueries = request.getParameter( PARAMETER_NO_RESULT ) != null;

        return redirectView( request, VIEW_MANAGE_SEARCH_STATS );
    }

    /**
     * Reads a positive date part from the request.
     *
     * @param request
     *            the HTTP request
     * @param strParameter
     *            the parameter name
     * @param nMax
     *            the highest accepted value
     * @return the value, or RecordFilter.NOT_DEFINED when it is missing or not between 1 and nMax
     */
    private static int getDateParameter( HttpServletRequest request, String strParameter, int nMax )
    {
        int nValue = NumberUtils.toInt( request.getParameter( strParameter ), RecordFilter.NOT_DEFINED );

        return ( nValue > 0 && nValue <= nMax ) ? nValue : RecordFilter.NOT_DEFINED;
    }

    /**
     * Builds the label of the detailed date.
     *
     * @param nYear
     *            the year
     * @param nMonth
     *            the month
     * @param nDay
     *            the day
     * @return the formatted date, empty when no year is given or the date does not exist
     */
    private String getDetailDateLabel( int nYear, int nMonth, int nDay )
    {
        String strDateLabel = "";

        if ( nYear != RecordFilter.NOT_DEFINED )
        {
            String strPattern;
            Calendar c = new GregorianCalendar( nYear, ( nMonth != RecordFilter.NOT_DEFINED ) ? nMonth - 1 : Calendar.JANUARY,
                    ( nDay != RecordFilter.NOT_DEFINED ) ? nDay : 1 );
            c.setLenient( false );

            if ( nMonth != RecordFilter.NOT_DEFINED )
            {
                if ( nDay != RecordFilter.NOT_DEFINED )
                {
                    strPattern = I18nService.getLocalizedString( PROPERTY_FORMAT_DAY, getLocale( ) );
                }
                else
                {
                    strPattern = I18nService.getLocalizedString( PROPERTY_FORMAT_MONTH, getLocale( ) );
                }
            }
            else
            {
                strPattern = I18nService.getLocalizedString( PROPERTY_FORMAT_YEAR, getLocale( ) );
            }

            try
            {
                strDateLabel = new SimpleDateFormat( strPattern, getLocale( ) ).format( c.getTime( ) );
            }
            catch( IllegalArgumentException e )
            {
                strDateLabel = "";
            }
        }

        return strDateLabel;
    }
}
