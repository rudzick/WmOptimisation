/*
 created at 17.07.2012

Copyright Marcus Bleil 2013

This file is part of Wmoptimisation.

Wmoptimisation is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Wmoptimisation is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Wmoptimisation.  If not, see <http://www.gnu.org/licenses/>.

Diese Datei ist Teil von Wmoptimisation.

Wmoptimisation ist Freie Software: Sie k�nnen es unter den Bedingungen
der GNU General Public License, wie von der Free Software Foundation,
Version 3 der Lizenz oder (nach Ihrer Option) jeder sp�teren
ver�ffentlichten Version, weiterverbreiten und/oder modifizieren.

Wmoptimisation wird in der Hoffnung, dass es n�tzlich sein wird, aber
OHNE JEDE GEW�HELEISTUNG, bereitgestellt; sogar ohne die implizite
Gew�hrleistung der MARKTF�HIGKEIT oder EIGNUNG F�R EINEN BESTIMMTEN ZWECK.
Siehe die GNU General Public License f�r weitere Details.

Sie sollten eine Kopie der GNU General Public License zusammen mit diesem
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.
 */
package org.athmis.wmoptimisation.changeset;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;
import org.simpleframework.xml.core.Commit;

/**
 * A ChangeSet object contains an OSM-Changeset, which is a container for single
 * changes on nodes or ways.
 * <p>
 * From <a
 * href="http://wiki.openstreetmap.org/wiki/API_v0.6#Changesets_2">OSM-Wiki</a>:
 * <blockquote cite="http://wiki.openstreetmap.org/wiki/API_v0.6#Changesets_2">
 * To avoid stale open changesets a mechanism is implemented to automatically
 * close changesets upon one of the following three conditions:
 * <ul>
 * <li>More than 50.000 edits on a single changeset</li>
 * <li>The changeset has been open for more than 24 hours</li>
 * <li>There have been no changes/API calls related to a changeset in 1 hour
 * (i.e. idle timeout)</li>
 * </ul>
 * </blockquote>
 * 
 * @author Marcus
 */
@Root(name = "changeset", strict = false)
public class ChangeSet implements Comparable<ChangeSet> {

	/**
	 * format conversation of osm date format: "yyyy-MM-dd'T'HH:mm:ss'Z'"
	 */
	@Transient
	public final static DateFormat OSM_DATE_TO_JAVA = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss'Z'");

	@Attribute(required = false)
	private double area;

	@Attribute(name = "closed_at", required = false)
	private String closedAt;

	// leave visibility "protected" for test purpose
	@Attribute(name = "created_at", required = false)
	protected String createdAt;

	/**
	 * corresponding to the id from OSM API call
	 */
	// leave visibility "protected" for test purpose
	@Attribute(name = "id")
	protected long id;

	@Attribute(name = "max_lat", required = false)
	private double maxLatitude;

	@Attribute(name = "max_lon", required = false)
	private double maxLongitude;

	@Attribute(name = "min_lat", required = false)
	private double minLatitude;

	@Attribute(name = "min_lon", required = false)
	private double minLongitude;

	@Attribute(name = "open", required = false)
	private boolean open;

	@ElementList(inline = true, required = false)
	private List<Tag> tags = new ArrayList<Tag>();

	@Attribute(name = "user")
	private String user;

	@Commit
	protected void calculateArea() {
		double width, heigth;

		heigth = Math.abs(maxLatitude - minLatitude);
		width = Math.abs(maxLongitude - minLongitude);

		area = heigth * width;
	}

	/**
	 * @throws IllegalArgumentException
	 *             if one created date has wrong format
	 * @see ChangeSet#OSM_DATE_TO_JAVA
	 */
	@Override
	public int compareTo(ChangeSet arg0) {
		Date meCreated;
		Date otherCreated;

		try {
			otherCreated = OSM_DATE_TO_JAVA.parse(arg0.createdAt);
			meCreated = OSM_DATE_TO_JAVA.parse(createdAt);
		} catch (ParseException e) {
			throw new IllegalArgumentException("created date has wrong format", e);
		}

		return meCreated.compareTo(otherCreated);
	}

	public double getArea() {
		return area;
	}

	public Calendar getClosed() throws ParseException {
		Calendar result = GregorianCalendar.getInstance();
		Date closed = OSM_DATE_TO_JAVA.parse(closedAt);
		result.setTime(closed);
		return result;
	}

	public String getClosedAt() {
		return closedAt;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public long getId() {
		return id;
	}

	public double getMaxLatitude() {
		return maxLatitude;
	}

	public double getMaxLongitude() {
		return maxLongitude;
	}

	public double getMinLatitude() {
		return minLatitude;
	}

	public double getMinLongitude() {
		return minLongitude;
	}

	public List<Tag> getTags() {
		return Collections.unmodifiableList(tags);
	}

	public String getUser() {
		return user;
	}

	public boolean isOpen() {
		return open;
	}
}