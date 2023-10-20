@file:Suppress("SpellCheckingInspection")

package com.example.googlemaptest

import android.app.Activity
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import com.example.googlemaptest.activites.AddPlaceActivity
import com.example.googlemaptest.application.FavoritePlacesApplication
import com.example.googlemaptest.models.Place
import com.example.googlemaptest.models.ResultMightThrow
import com.example.googlemaptest.network.Client
import com.example.googlemaptest.network.Server
import com.fasterxml.jackson.core.type.TypeReference
import com.google.common.truth.Truth.assertWithMessage
import edu.illinois.cs.cs125.gradlegrader.annotations.Graded
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.junit.After
import org.junit.Assert.fail
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode
import java.net.HttpURLConnection
import java.util.Random
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@RunWith(Enclosed::class)
class MP2Test {

    // Unit tests that don't require simulating the entire app
    @FixMethodOrder(MethodSorters.NAME_ASCENDING)
    class UnitTests {
        // Create an HTTP client to test the server with
        private val httpClient = OkHttpClient()

        init {
            // Start the API server
            Server.start()
        }

        @Before
        fun resetServer() {
            Server.reset()
        }

        private fun serverGetPlaces(): List<Place> {
            val placesRequest = Request.Builder().url(FavoritePlacesApplication.SERVER_URL + "/places/").build()
            val placesResponse: Response = httpClient.newCall(placesRequest).execute()
            assertWithMessage("Request should have succeeded")
                .that(placesResponse.isSuccessful)
                .isTrue()
            val body = placesResponse.body
            assertWithMessage("Response body should not be null").that(body).isNotNull()
            return objectMapper.readValue(body!!.string(), object : TypeReference<List<Place>>() {})
        }

        @Suppress("LongParameterList")
        private fun testServerFavoritePlacePostHelper(
            id: String?,
            name: String?,
            latitude: Double?,
            longitude: Double?,
            description: String?,
            expectedResponse: Int,
            expectedCount: Int = PLACES_COUNT
        ) {
            val newPlaceJSON: String = makePlaceJSON(id, name, latitude, longitude, description)
            val favoritePlaceRequest = Request.Builder()
                .url(FavoritePlacesApplication.SERVER_URL + "/favoriteplace/")
                .post(newPlaceJSON.toRequestBody("application/json; charset=utf-8".toMediaType()))
                .build()
            val favoritePlaceResponse: Response = httpClient.newCall(favoritePlaceRequest).execute()
            val message = if (expectedResponse == 200) {
                "Request should have succeeded"
            } else {
                "Request should not have succeeded"
            }
            assertWithMessage(message)
                .that(favoritePlaceResponse.code)
                .isEqualTo(expectedResponse)

            val newPlaces = serverGetPlaces()
            assertWithMessage("Wrong count of places after add").that(newPlaces).hasSize(expectedCount)
            val newPlace = newPlaces.find { it.id == id }
            if (expectedResponse == 200) {
                assertWithMessage("Should have added the place").that(newPlace).isNotNull()
            } else {
                assertWithMessage("Should not have added the place").that(newPlace).isNull()
            }
            if (expectedResponse != 200) {
                return
            }
            assertWithMessage("Wrong name on newly-added place").that(newPlace!!.name).isEqualTo(name)
            assertWithMessage("Wrong latitude on newly-added place").that(newPlace.latitude).isEqualTo(latitude)
            assertWithMessage("Wrong longitude on newly-added place").that(newPlace.longitude).isEqualTo(longitude)
            assertWithMessage("Wrong description on newly-added place")
                .that(newPlace.description)
                .isEqualTo(description)
        }

        @Suppress("LongMethod")
        @Graded(points = 20, friendlyName = "Test Server Add Place POST")
        @Test(timeout = 4000L)
        fun test0_ServerFavoritePlacePost() {
            val allPlaces = serverGetPlaces()
            assertWithMessage("Wrong initial number of places")
                .that(allPlaces.size)
                .isEqualTo(PLACES_COUNT)

            // Test invalid requests
            val invalidRequest1 = Request.Builder()
                .url(FavoritePlacesApplication.SERVER_URL + "/favoriteplace/")
                .post("dogs".toRequestBody("application/json; charset=utf-8".toMediaType()))
                .build()
            val invalidResponse1 = httpClient.newCall(invalidRequest1).execute()
            assertWithMessage("Request should not have succeeded")
                .that(invalidResponse1.code)
                .isEqualTo(HttpURLConnection.HTTP_BAD_REQUEST)

            val invalidRequest2: Request = Request.Builder()
                .url(FavoritePlacesApplication.SERVER_URL + "/favoriteplace/")
                .post("""{"cats": "true"}""".toRequestBody("application/json; charset=utf-8".toMediaType()))
                .build()
            val invalidResponse2: Response = httpClient.newCall(invalidRequest2).execute()
            assertWithMessage("Request should not have succeeded")
                .that(invalidResponse2.code)
                .isEqualTo(HttpURLConnection.HTTP_BAD_REQUEST)

            val invalidRequest3: Request = Request.Builder()
                .url(FavoritePlacesApplication.SERVER_URL + "/favoriteplace/")
                .post("""{"cats":\n"true"}""".toRequestBody("application/json; charset=utf-8".toMediaType()))
                .build()
            val invalidResponse3: Response = httpClient.newCall(invalidRequest3).execute()
            assertWithMessage("Request should not have succeeded")
                .that(invalidResponse3.code)
                .isEqualTo(HttpURLConnection.HTTP_BAD_REQUEST)

            // Test bad requests
            testServerFavoritePlacePostHelper(
                null,
                "Xyz Challen",
                88.8,
                -88.8,
                "Cat tent",
                HttpURLConnection.HTTP_BAD_REQUEST
            )
            testServerFavoritePlacePostHelper(
                "461f346d-963g-438b-9249-279eb925bfac",
                "Xyz Challen",
                88.8,
                -88.8,
                "Cat tent",
                HttpURLConnection.HTTP_BAD_REQUEST,
                PLACES_COUNT
            )
            testServerFavoritePlacePostHelper(
                "meow",
                "Xyz Challen",
                88.8,
                -88.8,
                "Cat tent",
                HttpURLConnection.HTTP_BAD_REQUEST
            )
            testServerFavoritePlacePostHelper(
                UUID.randomUUID().toString(),
                null,
                88.8,
                -88.8,
                "Cat tent",
                HttpURLConnection.HTTP_BAD_REQUEST
            )
            testServerFavoritePlacePostHelper(
                UUID.randomUUID().toString(),
                "Xyz Challen",
                null,
                -88.8,
                "Cat tent",
                HttpURLConnection.HTTP_BAD_REQUEST
            )
            testServerFavoritePlacePostHelper(
                UUID.randomUUID().toString(),
                "Xyz Challen",
                88.8,
                null,
                "Cat tent",
                HttpURLConnection.HTTP_BAD_REQUEST
            )
            testServerFavoritePlacePostHelper(
                UUID.randomUUID().toString(),
                "Xyz Challen",
                88.8,
                -88.8,
                null,
                HttpURLConnection.HTTP_BAD_REQUEST
            )
            testServerFavoritePlacePostHelper(
                "",
                "Xyz Challen",
                88.8,
                -88.8,
                "Cat tent",
                HttpURLConnection.HTTP_BAD_REQUEST
            )
            testServerFavoritePlacePostHelper(
                UUID.randomUUID().toString(),
                "",
                88.8,
                -88.8,
                "Cat tent",
                HttpURLConnection.HTTP_BAD_REQUEST
            )
            testServerFavoritePlacePostHelper(
                UUID.randomUUID().toString(),
                "Xyz Challen",
                88.8,
                -88.8,
                "",
                HttpURLConnection.HTTP_BAD_REQUEST
            )
            testServerFavoritePlacePostHelper(
                UUID.randomUUID().toString(),
                "Xyz Challen",
                88.8,
                -180.1,
                "Cat tent",
                HttpURLConnection.HTTP_BAD_REQUEST
            )
            testServerFavoritePlacePostHelper(
                UUID.randomUUID().toString(),
                "Xyz Challen",
                90.1,
                -179.9,
                "Cat tent",
                HttpURLConnection.HTTP_BAD_REQUEST
            )

            // Test OK requests
            val gracieUUID = UUID.randomUUID().toString()
            val xyzUUID = UUID.randomUUID().toString()

            testServerFavoritePlacePostHelper(
                gracieUUID,
                "Gracie Challen",
                88.8,
                -88.8,
                "Dog Park",
                HttpURLConnection.HTTP_OK,
                PLACES_COUNT + 1
            )
            testServerFavoritePlacePostHelper(
                gracieUUID,
                "Gracie Challen",
                88.8,
                -88.8,
                "Banjo's House",
                HttpURLConnection.HTTP_OK,
                PLACES_COUNT + 1
            )
            testServerFavoritePlacePostHelper(
                xyzUUID,
                "Xyz Challen",
                9.9,
                -9.9,
                "Under the bed",
                HttpURLConnection.HTTP_OK,
                PLACES_COUNT + 2
            )
            testServerFavoritePlacePostHelper(
                xyzUUID,
                "Xyz Challen",
                9.9,
                -9.9,
                "On your desk",
                HttpURLConnection.HTTP_OK,
                PLACES_COUNT + 2
            )
            assertWithMessage("Wrong number of places")
                .that(serverGetPlaces().size)
                .isEqualTo(PLACES_COUNT + 2)

            // Make sure 0.0 latitude and longitude work
            testServerFavoritePlacePostHelper(
                xyzUUID,
                "Xyz Challen",
                0.0,
                -9.9,
                "On your desk",
                HttpURLConnection.HTTP_OK,
                PLACES_COUNT + 2
            )
            testServerFavoritePlacePostHelper(
                xyzUUID,
                "Xyz Challen",
                -9.9,
                0.0,
                "On your desk",
                HttpURLConnection.HTTP_OK,
                PLACES_COUNT + 2
            )

            // Random testing stage
            Server.reset()
            assertWithMessage("Wrong number of places")
                .that(serverGetPlaces().size)
                .isEqualTo(PLACES_COUNT)

            val random = Random(124)
            val availableUUIDs = mutableSetOf<String>()
            val usedUUIDs = mutableSetOf<String>()
            repeat(32) {
                if (availableUUIDs.isEmpty() || random.nextBoolean()) {
                    availableUUIDs += UUID.randomUUID().toString()
                }
                val currentUUID = availableUUIDs.toTypedArray()[random.nextInt(availableUUIDs.size)]
                usedUUIDs += currentUUID

                val randomName = "" + random.nextInt()
                val randomDescription = "" + random.nextInt()
                val randomLatitude = 90.0 - 180.0 * random.nextDouble()
                val randomLongitude = 180.0 - 360.0 * random.nextDouble()

                testServerFavoritePlacePostHelper(
                    currentUUID,
                    randomName,
                    randomLatitude,
                    randomLongitude,
                    randomDescription,
                    HttpURLConnection.HTTP_OK,
                    PLACES_COUNT + usedUUIDs.size
                )
            }
        }
    }

    // Integration tests that require simulating the entire app
    @RunWith(AndroidJUnit4::class)
    @LooperMode(LooperMode.Mode.PAUSED)
    @Config(qualifiers = "w1080dp-h2088dp")
    @FixMethodOrder(MethodSorters.NAME_ASCENDING)
    class IntegrationTests {
        init {
            // Set up logging so that you can see log output during testing
            configureLogging()
        }

        @Before
        fun resetServer() {
            Server.reset()
        }

        // After each test make sure the client connected successfully
        @After
        fun checkClient() {
            assertWithMessage("Client should be connected").that(Client.connected).isTrue()
        }

        private fun clientGetPlaces(): List<Place> {
            // A CompletableFuture allows us to wait for the result of an asynchronous call
            val completableFuture = CompletableFuture<ResultMightThrow<List<Place>>>()
            // When getPlaces returns, it causes the CompletableFuture to complete
            Client.getPlaces { completableFuture.complete(it) }

            // Wait for the CompletableFuture to complete
            val result = try {
                completableFuture.get(1, TimeUnit.SECONDS)
            } catch (e: TimeoutException) {
                fail("GET did not complete in 1 second")
                throw e
            }
            assertWithMessage("getPlaces threw an exception").that(result.exception).isNull()
            val places = result.result

            // The List<Places> should not be null, which is returned by getPlaces when something
            // went wrong
            assertWithMessage("Request failed").that(places).isNotNull()
            return places
        }

        private fun clientPostPlace(place: Place, shouldThrow: Boolean) {
            // A CompletableFuture allows us to wait for the result of an asynchronous call
            val completableFuture = CompletableFuture<ResultMightThrow<Boolean>>()

            // When getPlaces returns, it causes the CompletableFuture to complete
            Client.postFavoritePlace(place) { completableFuture.complete(it) }

            // Wait for the CompletableFuture to complete
            val result = try {
                completableFuture.get(1, TimeUnit.SECONDS)
            } catch (e: TimeoutException) {
                fail("POST request did not complete in 1 second")
                throw e
            }
            if (!shouldThrow) {
                assertWithMessage("postFavoritePlace threw an exception")
                    .that(result.exception)
                    .isNull()
                assertWithMessage("Request failed").that(result.result).isTrue()
            } else {
                assertWithMessage("postFavoritePlace did not throw an exception")
                    .that(result.exception)
                    .isNotNull()
            }
        }

        @Graded(points = 20, friendlyName = "Test Client Add Place POST")
        @Test(timeout = 10000L)
        fun test1_ClientFavoritePlacePost() {
            var allPlaces = clientGetPlaces()
            assertWithMessage("Wrong initial number of places").that(allPlaces).hasSize(PLACES_COUNT)

            val newPlace =
                makePlaceFromJson(UUID.randomUUID().toString(), "namename", 88.8, -88.8, "descriptiondescription")
            clientPostPlace(newPlace, false)

            allPlaces = clientGetPlaces()
            assertWithMessage("Wrong number of places after POST").that(allPlaces).hasSize(PLACES_COUNT + 1)

            val invalidPlace: Place = makePlaceFromJson("not-a-uuid", "namename", 888.8, -888.8, "")
            clientPostPlace(invalidPlace, true)

            allPlaces = clientGetPlaces()
            assertWithMessage("Wrong number of places after POST").that(allPlaces).hasSize(PLACES_COUNT + 1)
        }

        @Graded(points = 20, friendlyName = "Test Long Press Launches Activity")
        @Test(timeout = 30000L)
        fun test2_LongPressLaunchesActivity() {
            val random = Random(124)
            startActivity()
                .onActivity { activity ->
                    val mapView: MapView = activity.findViewById(R.id.map)
                    val clickableMapView = ClickableMapView(mapView)
                    repeat(8) {
                        val randomCenter = randomGeoPointOnMap(random)
                        mapView.controller.setCenter(randomCenter)
                        mapView.controller.setZoom(19.0)
                        clickableMapView.update()
                        val randomClick = randomGeoPointInMap(random, mapView)
                        clickableMapView.longPress(randomClick)
                        pause()

                        // Check to make sure that the right Intent is launched
                        val started = shadowOf(activity)?.nextStartedActivity
                        assertWithMessage("Didn't start activity").that(started).isNotNull()
                        val shadowIntent = shadowOf(started!!)
                        assertWithMessage("Started wrong activity")
                            .that(shadowIntent.intentClass.simpleName)
                            .isEqualTo("AddPlaceActivity")
                        val latitude = started.getStringExtra("latitude")
                        assertWithMessage("Didn't launch activity properly: latitude null")
                            .that(latitude)
                            .isNotNull()
                        val longitude = started.getStringExtra("longitude")
                        assertWithMessage("Didn't launch activity properly: longitude null")
                            .that(longitude)
                            .isNotNull()
                        val activityPoint = GeoPoint(latitude!!.toDouble(), longitude!!.toDouble())
                        assertWithMessage("Didn't launch activity properly: latitude or longitude incorrect")
                            .that(compareGeopoints(activityPoint, randomClick))
                            .isTrue()
                    }
                }
        }

        @Graded(points = 20, friendlyName = "Test Add Place Activity")
        @Test(timeout = 60000L)
        fun test3_AddPlaceActivity() {
            val random = Random(124)
            val activityScenario = startActivity()
            activityScenario.onActivity { topMainActivity ->
                pause()

                val topMapView = topMainActivity.findViewById<MapView>(R.id.map)
                val topMarkers: List<Marker?> = getMarkers(topMapView)
                assertWithMessage("Wrong number of markers shown").that(topMarkers).hasSize(PLACES_COUNT)

                repeat(8) {
                    val randomPoint = randomGeoPointOnMap(random)
                    val randomDescription = "" + random.nextInt()
                    val intent =
                        Intent(ApplicationProvider.getApplicationContext(), AddPlaceActivity::class.java).apply {
                            putExtra("latitude", randomPoint.latitude.toString())
                            putExtra("longitude", randomPoint.longitude.toString())
                        }

                    ActivityScenario.launch<Activity>(intent)
                        .onActivity { addPlaceActivity ->
                            val editText = addPlaceActivity.findViewById<EditText>(R.id.description)
                            editText.setText(randomDescription)
                            val saveButton =
                                addPlaceActivity.findViewById<Button>(R.id.save_button)
                            saveButton.performClick()
                            pause()
                            val started = shadowOf(topMainActivity).nextStartedActivity
                            assertWithMessage("Didn't start activity").that(started).isNotNull()
                            val shadowIntent = shadowOf(started)
                            assertWithMessage("Started wrong activity")
                                .that(shadowIntent.intentClass.simpleName)
                                .isEqualTo("MainActivity")
                            ActivityScenario.launch<Activity>(started)
                                .onActivity { finishedMainActivity ->
                                    pause()

                                    val finishedMapView = finishedMainActivity.findViewById<MapView>(R.id.map)
                                    val finishedMarkers = getMarkers(finishedMapView)
                                    assertWithMessage("Wrong number of places shown")
                                        .that(finishedMarkers).hasSize(PLACES_COUNT + 1)

                                    val places = clientGetPlaces()
                                    assertWithMessage("Wrong number of places from API")
                                        .that(places).hasSize(PLACES_COUNT + 1)

                                    val found = places.find {
                                        it.latitude == randomPoint.latitude && it.longitude == randomPoint.longitude
                                    }
                                    assertWithMessage("Place not added properly")
                                        .that(found)
                                        .isNotNull()
                                    assertWithMessage("Added place has wrong ID")
                                        .that(found!!.id)
                                        .isEqualTo(FavoritePlacesApplication.CLIENT_ID)
                                    assertWithMessage("Added place has wrong description")
                                        .that(found.description)
                                        .isEqualTo(randomDescription)
                                }
                        }
                }
            }
        }
    }
}
// DO NOT REMOVE THIS LINE
// md5: 95b2d0866bb40fb24085b5bb86159805
