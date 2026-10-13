/*
 * Copyright (c) 2002-2026, Mairie de Paris
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
package fr.paris.lutece.plugins.searchstats.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import fr.paris.lutece.plugins.searchstats.business.QueryRecord;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests of TermRecordService
 */
public class TermRecordServiceTest
{
    /**
     * Counts each term of the queries and sorts the terms by decreasing count.
     */
    @Test
    public void testGetTopTerms( )
    {
        List<QueryRecord> listQueries = new ArrayList<>( );
        listQueries.add( newQuery( "paris velib" ) );
        listQueries.add( newQuery( "velib" ) );
        listQueries.add( newQuery( "piscine velib paris" ) );

        List<TermRecord> listTerms = TermRecordService.getTopTerms( listQueries );

        assertEquals( 3, listTerms.size( ) );
        assertEquals( "velib", listTerms.get( 0 ).getTerm( ) );
        assertEquals( 3, listTerms.get( 0 ).getCount( ) );
        assertEquals( "paris", listTerms.get( 1 ).getTerm( ) );
        assertEquals( 2, listTerms.get( 1 ).getCount( ) );
        assertEquals( "piscine", listTerms.get( 2 ).getTerm( ) );
        assertEquals( 1, listTerms.get( 2 ).getCount( ) );
    }

    /**
     * Returns no term for no query.
     */
    @Test
    public void testGetTopTermsEmpty( )
    {
        assertTrue( TermRecordService.getTopTerms( new ArrayList<>( ) ).isEmpty( ) );
    }

    /**
     * Builds a query record.
     *
     * @param strQuery
     *            the query
     * @return the query record
     */
    private static QueryRecord newQuery( String strQuery )
    {
        QueryRecord queryRecord = new QueryRecord( );
        queryRecord.setQuery( strQuery );

        return queryRecord;
    }
}
