<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format" exclude-result-prefixes="fo">
    <xsl:template match="book/article">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="simpleA4" page-height="29.7cm" page-width="21cm" margin-top="2cm" margin-bottom="2cm" margin-left="2cm" margin-right="2cm">
                    <fo:region-body/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="simpleA4">
                <fo:flow flow-name="xsl-region-body">
                    <!-- Invoice z dkb title-->
                    <fo:block font-size="16pt" font-weight="bold" space-after="5mm"><xsl:value-of select="title"/></fo:block>
                    <fo:block font-size="10pt" font-weight="bold" space-before="5mm" space-after="5mm" ><xsl:value-of select="subtitle[1]"/></fo:block>
                    <fo:block font-size="10pt">From: <xsl:value-of select="from"/></fo:block>
                    <fo:block font-size="10pt">To: <xsl:value-of select="to"/></fo:block>
                    <fo:block font-size="10pt" font-weight="bold" space-before="5mm" space-after="5mm" ><xsl:value-of select="subtitle[2]"/></fo:block>
                    <fo:block font-size="10pt">Forename: <xsl:value-of select="firstname"/></fo:block>
                    <fo:block font-size="10pt">Surname: <xsl:value-of select="surname"/></fo:block>
                    <fo:block font-size="10pt">Hourly wage: <xsl:value-of select="hourlyWage"/></fo:block>
                    <fo:block font-size="10pt">Hours in total: <xsl:value-of select="hoursInTotal"/></fo:block>
                    <fo:block font-size="10pt">Salary: <xsl:value-of select="salary"/></fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>