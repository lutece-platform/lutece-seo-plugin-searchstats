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
package fr.paris.lutece.plugins.searchstats.business;

import fr.paris.lutece.plugins.searchstats.business.QueryRecord;
import fr.paris.lutece.portal.service.plugin.Plugin;

import java.util.List;

/**
 * QueryRecordDAO Interface
 */
public interface IQueryRecordDAO
{
    /**
     * Insert a new record in the table.
     *
     *
     * @param queryRecord
     *            The queryRecord object
     * @param plugin
     *            The Plugin using this data access service
     */
    void insert( QueryRecord queryRecord, Plugin plugin );

    /**
     * Load the list of queryRecords
     * 
     * @param plugin
     *            The Plugin using this data access service
     * @param filter
     *            Filter containing criterias
     * @return The Collection of the QueryRecords
     */
    public List<QueryRecord> selectQueryRecordListByCriteria( Plugin plugin, RecordFilter filter );

    /**
     * Load the list of queryRecords
     * 
     * @param plugin
     *            The Plugin using this data access service
     * @param filter
     *            Filter containing criterias
     * @return The Collection of the QueryRecords
     */
    public List<QueryRecord> selectQueryRecordListFromDate( Plugin plugin, RecordFilter filter );

            
    /**
     * Load the list of RecordCount for each date that have at least a record
     * 
     * @param plugin
     *            The Plugin using this data access service
     * @return The Collection of the RecordCount
     */
    public List<RecordCount> selectQueryRecordDatesList( Plugin plugin, boolean bNoResult );

    /**
     * Load the list of queryRecords
     * 
     * @param plugin
     * 			  The Plugin using this data access service
     * @return The Collection of the QueryRecords
     */
    public List<QueryRecord> selectQueryRecordList( Plugin plugin );
}
