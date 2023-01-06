# climate
A portfolio project that allows a user to discover cities that have similar climate properties.

The idea originates from a simple concept: If I were to move to a different city, but want to retain the climate of my current, where could I go? Obviously, if I really love living in Portland Maine, moving to a tropical environment would be a significant change. The same applies to someone who lives in Arizona; moving to Seattle or London or Stockholm would mean a new wardrobe, although a reduced air conditioning bill would be a plus.

The data used by this service comes from the WMO, World Weather Information Service (https://worldweather.wmo.int). With it we are given monthly aggregate climate data (min/max temperatures, rainfall, # of rain days). This provides a basis for generating a list of similar cities to a given location.

It's actually quite amazing to look at the difference from city to city, and really dive into the actual conditions. I live in San Jose, but consider San Francisco to be fairly optimal conditions. What cities would be similar? Paris? Rome? Tokyo? Stockholm? We could get the climate data for each and compare, but to do so in a objective fashion, we need to have some analytic process. Plus, what about regions and geographies that aren't readily obvious? Could we add Capetown, Perth, and Istanbul to the list?

Hence, the similarity request. Filtering the cities under consideration by population (minPop and maxPop), it takes 4 monthly metrics (lowest temperature, highest temperature, rainfall, rain days),
aggregates them appropriately - monthly aggregates(mean) = avg temp, lowest temp, highest temp, annual aggregates of rainfall and rain days, and applies a similarity score to each city. Those cities are then passed back to the client, sorted by the similarity score.

So, to answer the original question: what locations are similar to San Francisco? According to the data given, there are 3 cities that meet the criteria:
* Barcelona, Spain
* Cadiz, Spain
* Haife, Israel

Not too shabby.

Of course, this all relies on the original dataset. There are significant completeness issues; most importantly, the monthly climate metrics are extreme-only -- lowest temperature and highest temperature recorded in the month. Mean temperature exists in the schema, but is not present in the data. Aggregating min/max monthly elevates potentially insignificant measurements beyond significance (one hot afternoon doesn't accurately reflect the entire month's temperature range). With smaller increments, or at least a consistent mean value, this could be solved for much easier and more accurately.

The reality of this of course is that the project herein isn't really about discovering climate characteristics (although it does work, within the constraints of the data). This project is intended to demonstrate several technical design and engineering concepts. Java, Spring, Mongodb are the basis here. The second 2 are fairly standard approaches, but the intent of the Java code is to try to write as cleanly as possible. Therefore, functional programming is used in many places. An early cut at this service, using abstract types and concrete implementations for behavior was more than double the raw lines of code as this. Now, the code uses a lot of Java streams and method references. The Load application (which is shockingly over-engineered for a one-time execution) uses some cool Future structures with thread pools, allowing for effective scaling and callback services.

Don't take this too seriously, it's mostly intended as practice for some of the more recent (or, recent to me) techniques, and to simply keep my design and engineering skills sharp.

Oh, and markdown makes me grumpy.
