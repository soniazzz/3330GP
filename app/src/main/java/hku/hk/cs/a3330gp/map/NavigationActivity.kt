package hku.hk.cs.a3330gp.map

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.location.Location
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.marginBottom
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDragHandleView
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.google.android.material.transition.platform.MaterialFadeThrough
import com.mapbox.api.directions.v5.models.Bearing
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.geojson.Point
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.navigation.base.TimeFormat
import com.mapbox.navigation.base.extensions.applyDefaultNavigationOptions
import com.mapbox.navigation.base.extensions.applyLanguageAndVoiceUnitOptions
import com.mapbox.navigation.base.formatter.DistanceFormatterOptions
import com.mapbox.navigation.base.options.NavigationOptions
import com.mapbox.navigation.base.route.NavigationRoute
import com.mapbox.navigation.base.route.NavigationRouterCallback
import com.mapbox.navigation.base.route.RouterFailure
import com.mapbox.navigation.base.route.RouterOrigin
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.core.directions.session.RoutesObserver
import com.mapbox.navigation.core.formatter.MapboxDistanceFormatter
import com.mapbox.navigation.core.lifecycle.MapboxNavigationApp
import com.mapbox.navigation.core.lifecycle.MapboxNavigationObserver
import com.mapbox.navigation.core.lifecycle.requireMapboxNavigation
import com.mapbox.navigation.core.replay.MapboxReplayer
import com.mapbox.navigation.core.replay.ReplayLocationEngine
import com.mapbox.navigation.core.replay.route.ReplayProgressObserver
import com.mapbox.navigation.core.replay.route.ReplayRouteMapper
import com.mapbox.navigation.core.trip.session.LocationMatcherResult
import com.mapbox.navigation.core.trip.session.LocationObserver
import com.mapbox.navigation.core.trip.session.RouteProgressObserver
import com.mapbox.navigation.ui.maneuver.api.MapboxManeuverApi
import com.mapbox.navigation.ui.maneuver.view.MapboxManeuverView
import com.mapbox.navigation.ui.maps.NavigationStyles
import com.mapbox.navigation.ui.maps.camera.NavigationCamera
import com.mapbox.navigation.ui.maps.camera.data.MapboxNavigationViewportDataSource
import com.mapbox.navigation.ui.maps.camera.lifecycle.NavigationBasicGesturesHandler
import com.mapbox.navigation.ui.maps.camera.state.NavigationCameraState
import com.mapbox.navigation.ui.maps.camera.transition.NavigationCameraTransitionOptions
import com.mapbox.navigation.ui.maps.camera.view.MapboxRecenterButton
import com.mapbox.navigation.ui.maps.camera.view.MapboxRouteOverviewButton
import com.mapbox.navigation.ui.maps.location.NavigationLocationProvider
import com.mapbox.navigation.ui.maps.route.arrow.api.MapboxRouteArrowApi
import com.mapbox.navigation.ui.maps.route.arrow.api.MapboxRouteArrowView
import com.mapbox.navigation.ui.maps.route.arrow.model.RouteArrowOptions
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineApi
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineView
import com.mapbox.navigation.ui.maps.route.line.model.MapboxRouteLineOptions
import com.mapbox.navigation.ui.tripprogress.api.MapboxTripProgressApi
import com.mapbox.navigation.ui.tripprogress.model.DistanceRemainingFormatter
import com.mapbox.navigation.ui.tripprogress.model.EstimatedTimeToArrivalFormatter
import com.mapbox.navigation.ui.tripprogress.model.PercentDistanceTraveledFormatter
import com.mapbox.navigation.ui.tripprogress.model.TimeRemainingFormatter
import com.mapbox.navigation.ui.tripprogress.model.TripProgressUpdateFormatter
import com.mapbox.navigation.ui.tripprogress.view.MapboxTripProgressView
import hku.hk.cs.a3330gp.R
import hku.hk.cs.a3330gp.ar.AttendanceActivity
import hku.hk.cs.a3330gp.util.Constants
import kotlinx.coroutines.launch
import java.util.Date

class NavigationActivity : AppCompatActivity(), MapDialogFragment.MapDialogListener {

    private companion object {
        private const val BUTTON_ANIMATION_DURATION = 1500L
        private const val BOTTOM_SHEET_PEEK_EXTRA = 0f
    }


    /**
     * Debug tool used to play, pause and seek route progress events that can be used to produce mocked location updates along the route.
     */
    private val mapboxReplayer = MapboxReplayer()

    /**
     * Debug tool that mocks location updates with an input from the [mapboxReplayer].
     */
    private val replayLocationEngine = ReplayLocationEngine(mapboxReplayer)

    /**
     * Debug observer that makes sure the replayer has always an up-to-date information to generate mock updates.
     */
    private val replayProgressObserver = ReplayProgressObserver(mapboxReplayer)


    private lateinit var mapView:MapView
    private lateinit var maneuverView:MapboxManeuverView
    private lateinit var tripProgressView:MapboxTripProgressView
    private lateinit var ivStop:ImageView
    private lateinit var recenter:MapboxRecenterButton
    private lateinit var routeOverview:MapboxRouteOverviewButton
    private lateinit var tripProgressCard:RelativeLayout
    private lateinit var sheetHandle: BottomSheetDragHandleView
    private lateinit var tvStatus:TextView
    private lateinit var btnAttendance:Button
    private lateinit var loadingView:FrameLayout

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<RelativeLayout>

    private lateinit var viewportDataSource: MapboxNavigationViewportDataSource
    private lateinit var navigationCamera: NavigationCamera
    private lateinit var maneuverApi: MapboxManeuverApi
    private lateinit var tripProgressApi: MapboxTripProgressApi
    private lateinit var routeLineApi: MapboxRouteLineApi
    private lateinit var routeLineView: MapboxRouteLineView
    private val routeArrowApi: MapboxRouteArrowApi = MapboxRouteArrowApi()
    private lateinit var routeArrowView: MapboxRouteArrowView

    private val pixelDensity = Resources.getSystem().displayMetrics.density
    private val overviewPadding: EdgeInsets by lazy {
        EdgeInsets(
            140.0 * pixelDensity,
            40.0 * pixelDensity,
            120.0 * pixelDensity,
            40.0 * pixelDensity
        )
    }
    private val landscapeOverviewPadding: EdgeInsets by lazy {
        EdgeInsets(
            30.0 * pixelDensity,
            380.0 * pixelDensity,
            110.0 * pixelDensity,
            20.0 * pixelDensity
        )
    }
    private val followingPadding: EdgeInsets by lazy {
        EdgeInsets(
            180.0 * pixelDensity,
            40.0 * pixelDensity,
            150.0 * pixelDensity,
            40.0 * pixelDensity
        )
    }
    private val landscapeFollowingPadding: EdgeInsets by lazy {
        EdgeInsets(
            30.0 * pixelDensity,
            380.0 * pixelDensity,
            110.0 * pixelDensity,
            40.0 * pixelDensity
        )
    }

    private val navigationLocationProvider = NavigationLocationProvider()
    private lateinit var locationObserver: LocationObserver
    private lateinit var routeProgressObserver: RouteProgressObserver
    private lateinit var routesObserver: RoutesObserver
    private val mapboxNavigation: MapboxNavigation by requireMapboxNavigation(
        onResumedObserver = object : MapboxNavigationObserver {
            @SuppressLint("MissingPermission")
            override fun onAttached(mapboxNavigation: MapboxNavigation) {
                mapboxNavigation.registerRoutesObserver(routesObserver)
                mapboxNavigation.registerLocationObserver(locationObserver)
                mapboxNavigation.registerRouteProgressObserver(routeProgressObserver)
                mapboxNavigation.registerRouteProgressObserver(replayProgressObserver)
                // start the trip session to being receiving location updates in free drive
                // and later when a route is set also receiving route progress updates
                mapboxNavigation.startTripSession()
            }

            override fun onDetached(mapboxNavigation: MapboxNavigation) {
                mapboxNavigation.unregisterRoutesObserver(routesObserver)
                mapboxNavigation.unregisterLocationObserver(locationObserver)
                mapboxNavigation.unregisterRouteProgressObserver(routeProgressObserver)
                mapboxNavigation.unregisterRouteProgressObserver(replayProgressObserver)
            }
        },
        onInitialize = this::initNavigation
    )

    private var isLoading = false
        set(value) {
            field = value
            loadingView.isGone = !value
        }

    private var selectedPatient = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)


        mapView = findViewById(R.id.mapView)
        maneuverView = findViewById(R.id.maneuverView)
        tripProgressView = findViewById(R.id.tripProgressView)
        ivStop = findViewById(R.id.ivStop)
        recenter = findViewById(R.id.recenter)
        routeOverview = findViewById(R.id.routeOverview)
        tripProgressCard = findViewById(R.id.tripProgressCard)
        sheetHandle = findViewById(R.id.sheetHandle)
        tvStatus = findViewById(R.id.tvStatus)
        btnAttendance = findViewById(R.id.btnAttendance)
        loadingView = findViewById(R.id.loadingView)

        bottomSheetBehavior = BottomSheetBehavior.from(tripProgressCard)

        setupAnimation()
        isLoading = true
        loadingView.bringToFront()
        // Navigation Camera
        viewportDataSource = MapboxNavigationViewportDataSource(mapView.getMapboxMap())
        navigationCamera = NavigationCamera(
            mapView.getMapboxMap(),
            mapView.camera,
            viewportDataSource
        )

        mapView.camera.addCameraAnimationsLifecycleListener(
            NavigationBasicGesturesHandler(navigationCamera)
        )
        navigationCamera.registerNavigationCameraStateChangeObserver { navigationCameraState ->
            // shows/hide the recenter button depending on the camera state
            when (navigationCameraState) {
                NavigationCameraState.TRANSITION_TO_FOLLOWING,
                NavigationCameraState.FOLLOWING -> recenter.visibility = View.INVISIBLE
                NavigationCameraState.TRANSITION_TO_OVERVIEW,
                NavigationCameraState.OVERVIEW,
                NavigationCameraState.IDLE -> recenter.visibility = View.VISIBLE
            }
        }
        // set the padding values depending on screen orientation and visible view layout
        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            viewportDataSource.overviewPadding = landscapeOverviewPadding
        } else {
            viewportDataSource.overviewPadding = overviewPadding
        }
        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            viewportDataSource.followingPadding = landscapeFollowingPadding
        } else {
            viewportDataSource.followingPadding = followingPadding
        }

        // Api data for top banner maneuver View
        val distanceFormatterOptions = DistanceFormatterOptions.Builder(this).build()
        maneuverApi = MapboxManeuverApi(
            MapboxDistanceFormatter(distanceFormatterOptions)
        )

        // Trip progress view
        tripProgressApi = MapboxTripProgressApi(
            TripProgressUpdateFormatter.Builder(this)
                .distanceRemainingFormatter(
                    DistanceRemainingFormatter(distanceFormatterOptions)
                )
                .timeRemainingFormatter(
                    TimeRemainingFormatter(this)
                )
                .percentRouteTraveledFormatter(
                    PercentDistanceTraveledFormatter()
                )
                .estimatedTimeToArrivalFormatter(
                    EstimatedTimeToArrivalFormatter(this, TimeFormat.NONE_SPECIFIED)
                )
                .build()
        )

        // initialize route line, the withRouteLineBelowLayerId is specified to place
        // the route line below road labels layer on the map
        // the value of this option will depend on the style that you are using
        // and under which layer the route line should be placed on the map layers stack
        val mapboxRouteLineOptions = MapboxRouteLineOptions.Builder(this)
            .withRouteLineBelowLayerId("road-label-navigation")
            .build()
        routeLineApi = MapboxRouteLineApi(mapboxRouteLineOptions)
        routeLineView = MapboxRouteLineView(mapboxRouteLineOptions)

        // initialize maneuver arrow view to draw arrows on the map
        val routeArrowOptions = RouteArrowOptions.Builder(this).build()
        routeArrowView = MapboxRouteArrowView(routeArrowOptions)

        // load map style
        lifecycleScope.launch {
            val mapStyle = when (this@NavigationActivity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_NO -> NavigationStyles.NAVIGATION_DAY_STYLE
                Configuration.UI_MODE_NIGHT_YES -> NavigationStyles.NAVIGATION_NIGHT_STYLE
                else -> NavigationStyles.NAVIGATION_DAY_STYLE
            }
            mapView.getMapboxMap().loadStyleUri(mapStyle) {
                // add long click listener that search for a route to the clicked destination
                mapView.gestures.addOnMapLongClickListener { point ->
                    setupBottomSheetPeekHeight()
                    // TODO: Remove Mapbox Replayer when done
                    findRoute(point)
                    true
                }
                isLoading = false
            }

        }

        // initialize view interactions
        ivStop.setOnClickListener {
            clearRouteAndStopNavigation()
        }
        recenter.setOnClickListener {
            navigationCamera.requestNavigationCameraToFollowing()
            routeOverview.showTextAndExtend(BUTTON_ANIMATION_DURATION)
        }
        routeOverview.setOnClickListener {
            navigationCamera.requestNavigationCameraToOverview()
            recenter.showTextAndExtend(BUTTON_ANIMATION_DURATION)
        }
        btnAttendance.setOnClickListener {
            selectPatient()
        }

        tripProgressCard.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // Ensure this listener is called only once
                tripProgressCard.viewTreeObserver.removeOnGlobalLayoutListener(this)
                setupBottomSheet()
            }
        })

        setupLocationObserver()
        setupRouteProgressObserver()
        setupRoutesObserver()
    }

    override fun onStart() {
        super.onStart()
        setupBottomSheet()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapboxReplayer.finish()
        maneuverApi.cancel()
        routeLineApi.cancel()
        routeLineView.cancel()
    }

    private fun setupLocationObserver() {
        locationObserver = object : LocationObserver {
            var firstLocationUpdateReceived = false

            override fun onNewRawLocation(rawLocation: Location) {
                // not handled
            }

            override fun onNewLocationMatcherResult(locationMatcherResult: LocationMatcherResult) {
                val enhancedLocation = locationMatcherResult.enhancedLocation
                // update location puck's position on the map
                navigationLocationProvider.changePosition(
                    location = enhancedLocation,
                    keyPoints = locationMatcherResult.keyPoints,
                )

                // update camera position to account for new location
                viewportDataSource.onLocationChanged(enhancedLocation)
                viewportDataSource.evaluate()

                // if this is the first location update the activity has received,
                // it's best to immediately move the camera to the current user location
                if (!firstLocationUpdateReceived) {
                    firstLocationUpdateReceived = true
                    navigationCamera.requestNavigationCameraToOverview(
                        stateTransitionOptions = NavigationCameraTransitionOptions.Builder()
                            .maxDuration(0) // instant transition
                            .build()
                    )
                }
            }
        }
    }

    private fun setupRouteProgressObserver() {
        routeProgressObserver = RouteProgressObserver { routeProgress ->
            // update the camera position to account for the progressed fragment of the route
            viewportDataSource.onRouteProgressChanged(routeProgress)
            viewportDataSource.evaluate()

            // draw the upcoming maneuver arrow on the map
            val style = mapView.getMapboxMap().getStyle()
            if (style != null) {
                val maneuverArrowResult = routeArrowApi.addUpcomingManeuverArrow(routeProgress)
                routeArrowView.renderManeuverUpdate(style, maneuverArrowResult)
            }

            // update top banner with maneuver instructions
            val maneuvers = maneuverApi.getManeuvers(routeProgress)
            maneuvers.fold(
                { error ->
                    Toast.makeText(
                        this,
                        error.errorMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                },
                {
                    maneuverView.visibility = View.VISIBLE
                    maneuverView.renderManeuvers(maneuvers)
                }
            )

            // update bottom trip progress summary
            tripProgressView.render(
                tripProgressApi.getTripProgress(routeProgress)
            )

            // update status text
            val desiredText: String
            if (tripProgressApi.getTripProgress(routeProgress).distanceRemaining <= 101) {
                updateInRange()
            } else {
                updateOutRange()
            }
            setupBottomSheetPeekHeight()
        }
    }

    private fun setupRoutesObserver() {
        routesObserver = RoutesObserver { routeUpdateResult ->
            if (routeUpdateResult.navigationRoutes.isNotEmpty()) {
                // generate route geometries asynchronously and render them
                routeLineApi.setNavigationRoutes(
                    routeUpdateResult.navigationRoutes
                ) { value ->
                    mapView.getMapboxMap().getStyle()?.apply {
                        routeLineView.renderRouteDrawData(this, value)
                    }
                }

                // update the camera position to account for the new route
                viewportDataSource.onRouteChanged(routeUpdateResult.navigationRoutes.first())
                viewportDataSource.evaluate()
            } else {
                // remove the route line and route arrow from the map
                val style = mapView.getMapboxMap().getStyle()
                if (style != null) {
                    routeLineApi.clearRouteLine { value ->
                        routeLineView.renderClearRouteLineValue(
                            style,
                            value
                        )
                    }
                    routeArrowView.render(style, routeArrowApi.clearArrows())
                }

                // remove the route reference from camera position evaluations
                viewportDataSource.clearRouteData()
                viewportDataSource.evaluate()
            }
        }
    }

    private fun setupBottomSheetPeekHeight() {
        val peekHeight = BOTTOM_SHEET_PEEK_EXTRA +
                tripProgressCard.paddingTop +
                sheetHandle.height +
                tvStatus.height +
                tvStatus.marginBottom / 2
        bottomSheetBehavior.peekHeight = peekHeight.toInt()
    }

    private fun setupBottomSheet() {
        setupBottomSheetPeekHeight()
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                bottomSheet.bringToFront()
            }
        })

    }

    private fun initNavigation() {
        MapboxNavigationApp.setup(
            NavigationOptions.Builder(this)
                .accessToken(getString(R.string.mapbox_access_token))
                // comment out the location engine setting block to disable simulation
                .locationEngine(replayLocationEngine)
                .build()
        )

        // initialize location puck
        mapView.location.apply {
            setLocationProvider(navigationLocationProvider)
            this.locationPuck = LocationPuck2D(
                bearingImage = ContextCompat.getDrawable(
                    this@NavigationActivity,
                    R.drawable.mapbox_navigation_puck_icon
                )
            )
            enabled = true
        }

        replayOriginLocation()
    }

    private fun replayOriginLocation() {
        mapboxReplayer.pushEvents(
            listOf(
                ReplayRouteMapper.mapToUpdateLocation(
                    Date().time.toDouble(),
                    Point.fromLngLat(114.136598, 22.283709)
                )
            )
        )
        mapboxReplayer.playFirstLocation()
        mapboxReplayer.playbackSpeed(8.0)
    }

    private fun findRoute(destination: Point) {
        val originLocation = navigationLocationProvider.lastLocation
        val originPoint = originLocation?.let {
            Point.fromLngLat(it.longitude, it.latitude)
        } ?: return

        // execute a route request
        // it's recommended to use the
        // applyDefaultNavigationOptions and applyLanguageAndVoiceUnitOptions
        // that make sure the route request is optimized
        // to allow for support of all of the Navigation SDK features
        mapboxNavigation.requestRoutes(
            RouteOptions.builder()
                .applyDefaultNavigationOptions()
                .applyLanguageAndVoiceUnitOptions(this)
                .coordinatesList(listOf(originPoint, destination))
                // provide the bearing for the origin of the request to ensure
                // that the returned route faces in the direction of the current user movement
                .bearingsList(
                    listOf(
                        Bearing.builder()
                            .angle(originLocation.bearing.toDouble())
                            .degrees(45.0)
                            .build(),
                        null
                    )
                )
                .layersList(listOf(mapboxNavigation.getZLevel(), null))
                .build(),
            object : NavigationRouterCallback {
                override fun onCanceled(routeOptions: RouteOptions, routerOrigin: RouterOrigin) {
                    // no impl
                }

                override fun onFailure(reasons: List<RouterFailure>, routeOptions: RouteOptions) {
                    // no impl
                }

                override fun onRoutesReady(
                    routes: List<NavigationRoute>,
                    routerOrigin: RouterOrigin
                ) {
                    setRouteAndStartNavigation(routes)
                }
            }
        )
    }

    private fun setRouteAndStartNavigation(routes: List<NavigationRoute>) {
        // set routes, where the first route in the list is the primary route that
        // will be used for active guidance
        mapboxNavigation.setNavigationRoutes(routes)

        // show UI elements
        routeOverview.visibility = View.VISIBLE
        tripProgressCard.visibility = View.VISIBLE

        // move the camera to overview when new route is available
        navigationCamera.requestNavigationCameraToOverview()
    }

    private fun clearRouteAndStopNavigation() {
        // clear
        mapboxNavigation.setNavigationRoutes(listOf())

        // stop simulation
        mapboxReplayer.stop()

        // hide UI elements
        maneuverView.visibility = View.INVISIBLE
        routeOverview.visibility = View.INVISIBLE
        updatePatientSelect()
    }

    private fun selectPatient() {
        MapDialogFragment().show(supportFragmentManager, "MAP_DIALOG")
    }

    private fun onAttendanceClick() {
        val intent = Intent(this, AttendanceActivity::class.java)
        startActivity(intent)
//        finish()
    }

    private fun setupAnimation() {
        // Animations
        when(intent.getStringExtra(Constants.TRANSITION)) {
            Constants.TRANSITION_MORPH -> {
                tripProgressCard.transitionName = "shared_map"
                setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
                window.sharedElementEnterTransition = MaterialContainerTransform().apply {
                    addTarget(R.id.tripProgressCard)
                    duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
                }
                window.sharedElementReturnTransition = MaterialContainerTransform().apply {
                    addTarget(R.id.tripProgressCard)
                    duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
                }
            }
            else -> {
                val enter = MaterialFadeThrough().apply {
                    addTarget(android.R.id.content)
                }
                window.enterTransition = enter
                window.allowEnterTransitionOverlap = true
            }
        }
    }

    private fun updatePatientSelect() {
        tvStatus.text = getString(R.string.btnAttendance_select_patient)
        btnAttendance.text = getString(R.string.btnAttendance_select_patient)
        btnAttendance.isEnabled = true
        btnAttendance.setOnClickListener { selectPatient() }
    }
    private fun updateOutRange() {
        tvStatus.text = getString(
            R.string.tvStatus_text_out_of_range,
            Constants.getUsers()[selectedPatient].name
        )
        btnAttendance.isEnabled = false
        btnAttendance.text = resources.getString(R.string.btnAttendance_text_out_of_range)
    }
    private fun updateInRange() {
        tvStatus.text = getString(R.string.tvStatus_text_in_range)
        btnAttendance.text = getString(R.string.btnAttendance_text_in_range)
        btnAttendance.isEnabled = true
        btnAttendance.setOnClickListener { onAttendanceClick() }
    }
    override fun onDialogPositiveClick(dialog: DialogFragment) {
        findRoute(Constants.getUsers()[selectedPatient].location)
        updateOutRange()
    }

    override fun onDialogNeutralClick(dialog: DialogFragment) {
        dialog.dismiss()
    }

    override fun onChooseItem(dialog: DialogFragment, choice: Int) {
        selectedPatient = choice
    }
}