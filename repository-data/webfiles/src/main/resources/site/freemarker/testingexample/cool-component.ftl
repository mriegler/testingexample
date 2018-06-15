<#-- @ftlvariable name="weatherInformation" type="org.example.beans.WeatherInformation" -->
<div class="cool-component">
    <h1>Cool Component</h1>
    <p>Is this cool? <#if cool>Yes it is.<#else>No it isnt</#if></p>

    <p>How is the weather in ${locationInfo.city}?</p>
    <p>Temp is: ${weatherInformation.main.temp}C</p>
</div>
