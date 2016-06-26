<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:import href="docbook-xsl/fo/docbook.xsl" />


    <xsl:template match="/invoice">
        <book>
            <article>
                <title>Invoice</title>
                <iid><xsl:value-of select="iid"/></iid>
                <subtitle>Date</subtitle>
                <from><xsl:value-of select="date/from"/></from>
                <to><xsl:value-of select="date/to"/></to>
                <xsl:apply-templates select="employee"/>
            </article>
        </book>
    </xsl:template>
    <xsl:template match="employee">
        <subtitle>Employee</subtitle>
        <eid><xsl:value-of select="eid"/></eid>
        <firstname><xsl:value-of select="forname"/></firstname>
        <surname><xsl:value-of select="surname"/></surname>
        <hourlyWage><xsl:value-of select="hourlyWage"/></hourlyWage>
        <hoursInTotal><xsl:value-of select="sum(//revenue/hours)"/></hoursInTotal>
        <salary><xsl:value-of select="sum(//revenue/totalSalary)"/></salary>
    </xsl:template>

</xsl:stylesheet>
