package com.tvd12.ezyfoxserver.database.query;

public interface EzyPushOptions {

	/**
     * Sets the position for the update
     * @param position the position in the array for the update
     * @return this
     */
    EzyPushOptions position(int position);

    /**
     * Sets the slice value for the update
     * @param slice the slice value for the update
     * @return this
     */
    EzyPushOptions slice(int slice);

    /**
     * Sets the sort value for the update
     * @param sort the sort value for the update
     * @return this
     */
    EzyPushOptions sort(int sort);

    /**
     * Sets the sort value for the update
     *
     * @param field     the field to sort by
     * @param direction the direction of the sort
     * @return this
     */
    EzyPushOptions sort(String field, int direction);
}
