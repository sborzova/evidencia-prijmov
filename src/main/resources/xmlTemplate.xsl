<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">

    <xsl:import href="docbook-xsl/fo/docbook.xsl" />

    <xsl:template match="/invoice">
        <book>
            <xsl:apply-templates select="person"/>
        </book>
    </xsl:template>
    <xsl:template match="person">
        <name username="{@username}">
            <xsl:value-of select="name" />
        </name>
    </xsl:template>

</xsl:stylesheet>
