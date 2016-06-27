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
                    <fo:block font-size="20pt" font-weight="bold" text-align="center" space-after="10mm" border-after-style="inset" border-after-width="medium" border-color="rgb(102,255,255)">
                        <xsl:value-of select="title"/>
                    </fo:block>
                    <fo:table width="80mm" border-style="solid" border-width="medium">
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell width="20mm" border-style="solid" border-width="medium" background-color="rgb(204,255,255)">
                                <fo:block margin="5pt" font-size="10pt" font-weight="bold" text-align="center">
                                    ID
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell width="30mm" border-style="solid" border-width="medium" background-color="rgb(204,255,255)">
                                <fo:block margin="5pt" font-size="10pt" font-weight="bold" text-align="center">
                                    DATE FROM
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell width="30mm" border-style="solid" border-width="medium" background-color="rgb(204,255,255)">
                                <fo:block margin="5pt" font-size="10pt" font-weight="bold" text-align="center">
                                    DATE TO
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell width="20mm" border-style="solid" border-width="1pt">
                                <fo:block margin="5pt" font-size="10pt" text-align="center">
                                    <xsl:value-of select="iid"/>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell width="30mm" border-style="solid" border-width="1pt">
                                <fo:block margin="5pt" font-size="10pt" text-align="center">
                                    <xsl:value-of select="from"/>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell width="30mm" border-style="solid" border-width="1pt">
                                <fo:block margin="5pt" font-size="10pt" text-align="center">
                                    <xsl:value-of select="to"/>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                    </fo:table>
                    <fo:block font-size="14pt" font-weight="bold" space-before="20mm" space-after="5mm" ><xsl:value-of select="subtitle[2]"/></fo:block>
                    <fo:table width="110mm" border-style="solid" border-width="medium">
                        <fo:table-body>
                            <fo:table-row>
                                <fo:table-cell width="30mm" border-style="solid" border-width="medium" background-color="rgb(204,255,255)">
                                    <fo:block margin="5pt" font-size="10pt" font-weight="bold" text-align="left">
                                        Forename
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell width="40mm" border-style="solid" border-width="medium">
                                    <fo:block margin="5pt" font-size="10pt"  text-align="right">
                                        <xsl:value-of select="firstname"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                            <fo:table-row>
                                <fo:table-cell width="30mm" border-style="solid" border-width="medium" background-color="rgb(204,255,255)">
                                    <fo:block margin="5pt" font-size="10pt" font-weight="bold" text-align="left">
                                        Surname
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell width="40mm" border-style="solid" border-width="medium">
                                    <fo:block margin="5pt" font-size="10pt"  text-align="right">
                                        <xsl:value-of select="surname"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                            <fo:table-row>
                                <fo:table-cell width="30mm" border-style="solid" border-width="medium" background-color="rgb(204,255,255)">
                                    <fo:block margin="5pt" font-size="10pt" font-weight="bold" text-align="left">
                                        Hourly wage
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell width="40mm" border-style="solid" border-width="medium">
                                    <fo:block margin="5pt" font-size="10pt"  text-align="right">
                                        <xsl:value-of select="hourlyWage"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                            <fo:table-row>
                                <fo:table-cell width="30mm" border-style="solid" border-width="medium" background-color="rgb(204,255,255)">
                                    <fo:block margin="5pt" font-size="10pt" font-weight="bold" text-align="left">
                                        Hours in total
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell width="40mm" border-style="solid" border-width="medium">
                                    <fo:block margin="5pt" font-size="10pt"  text-align="right">
                                        <xsl:value-of select="hoursInTotal"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                            <fo:table-row>
                                <fo:table-cell width="30mm" border-style="solid" border-width="medium" background-color="rgb(204,255,255)">
                                    <fo:block margin="5pt" font-size="10pt" font-weight="bold" text-align="left">
                                        Salary
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell width="40mm" border-style="solid" border-width="medium">
                                    <fo:block margin="5pt" font-size="10pt"  text-align="right" text-decoration="underline">
                                        <xsl:value-of select="salary"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>