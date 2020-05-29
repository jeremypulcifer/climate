# climate
Portfolio Project that allows a user to discover cities that have similar climate properties.

The idea originates from a simple concept: If I were to move to a different city, but want to retain the climate of my current, where could I go? Obviously, if I really love living in Portland Maine, moving to a tropical environment would be a significant change. The same applies to someone who lives in Arizona; moving to Seattle or London or Stockholm would mean a new wardrobe, although a reduced air conditioning bill would be a plus.

The data comes from the WMO, World Weather Information Service (https://worldweather.wmo.int). With it we are given monthly aggregate climate data (min/max temperatures, rainfall, # of rain days). This provides a basis for generating a list of similar cities to a given location.

It's actually quite amazing to look at the difference from city to city, and really dive into the actual conditions. I live in San Jose, but consider San Francisco to be fairly optimal conditions. What cities would be similar? Paris? Rome? Tokyo? Stockholm? We could get the climate data for each and compare, but to do so in a objective fashion, we need to have some analytic process. Plus, what about regions and geographies that aren't readily obvious? Could we add Capetown, Perth, and Istanbul to the list?

Hence, the similarity request. This takes 4 monthly metrics (lowest temp, highest temp, rainfall, rain days), aggregates them appropriately - monthly aggregates are mean of lowest+highest = avg temp, lowest temp, highest temp, annual aggregates are rainfall and rain days, and applies a similarity comparison. That algorithm does 2 things to the result of the potentially-similar city: it removes a specified number of outlier values (the n-hottest and n-coolest) to the monthly list of aggregates so as to smooth out the out of range variances, and then applies multipliers to give additional range to the match. If the potentially-similar city exists within those values, it is considered similar. All similar cities are then passed back to the client.

So, to answer the original question: what locations are similar to San Francisco? According to the data given, there are 3 cities that meet the criteria:
* Funchal, in the Atlantic off the coast of Morocco
* Tarifa, on the southern coast of Spain
* Lisboa (Lisbon), Portugal

Not too shabby.

Currently, the similarity range is statically set in the code at allowing 20% higher temperatures, 65% higher rain days per year, and 30% higher total annual rainfall. These factors are somewhat arbitrary; in the future I could put a mechanism to inject variances upon personal preference (i.e. I would accept more rain, or less, a few degrees warmer, etc). Looking for similarities with, say, Seattle doesn't fit the existing model extremely well; 34 cities match Seattle with the assumed range factors, which is too many to really use. The (apparent) arbitrary variance values in place reflect the struggle to find cities similar to San Francisco; while SF is quite temperate (if on the cool-ish side), it is also remarkably dry. If the temperature range is increased to the point where adequate number of results is found, the collection has a decided 'desert' quality. Adding enough rain days/fall means heavy percipitation. Neither is particularly acceptable for my personal purposes. Therefore I tried various combinations of factors that still reflected my personal desires, until the resulting list was meaningful. Not exceedly rigorous, but a start.

Of course, this all relies on the original dataset. There are significant completeness issues; most importantly, the monthly climate metrics are extreme-only -- lowest temperature and highest temperature recorded in the month. Mean temperature exists in the schema, but is not present in the data. Aggregating min/max monthly elevates potentially insignificant measurements beyond significance (one hot afternoon doesn't accurately reflect the entire month's temperature range). With smaller increments, or at least a consistent mean value, this could be solved for much easier and more accurately.

The reality of this of course is that the project herein isn't really about discovering climate characteristics (although it does work, within the constraints of the data). This project is intended to demonstrate several technical design and engineering concepts. Java, Spring, Mongodb are the basis here. The second 2 are fairly standard approaches, but the intent of the Java code is to try to write as cleanly as possible. Therefore, functional programming is used in many places. An early cut at this service, using abstract types and concrete implementations for behavior was more than double the raw lines of code as this. Now, the code uses a lot of Java streams and method references. The Load application (which is shockingly over-engineered for a one-time execution) uses some cool Future structures with thread pools, allowing for effective scaling and callback services.

Don't take this too seriously, it's mostly intended as practice for some of the more recent (or, recent to me) techniques, and to simply keep my design and engineering skills sharp.

Oh, and markdown makes me grumpy.
