package com.homedelivery.web;

import com.homedelivery.model.exportDTO.CommentsViewInfo;
import com.homedelivery.model.importDTO.AddCommentDTO;
import com.homedelivery.service.interfaces.CommentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService mockCommentService;

    @Test
    public void testViewComments() throws Exception {
        CommentsViewInfo commentsViewInfo = new CommentsViewInfo();

        when(mockCommentService.getAllComments()).thenReturn(commentsViewInfo);

        mockMvc.perform(get("/comments"))
                .andExpect(status().isOk())
                .andExpect(view().name("comments"))
                .andExpect(model().attributeExists("comments"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testAddCommentGet() throws Exception {
        mockMvc.perform(get("/comments/add-comment"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-comment"))
                .andExpect(model().attributeExists("addCommentDTO"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testAddCommentPostSuccess() throws Exception {
        when(mockCommentService.addComment(any(AddCommentDTO.class))).thenReturn(true);

        mockMvc.perform(post("/comments/add-comment")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("description", "This is a test comment")
                        .param("rating", "2")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/comments"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testAddCommentPost_ValidationErrors() throws Exception {

        mockMvc.perform(post("/comments/add-comment")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("description", "")
                        .param("rating", "0")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("add-comment"))
                .andExpect(model().attributeExists("addCommentDTO"))
                .andExpect(model().attributeHasFieldErrors("addCommentDTO", "description", "rating"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testDeleteComment() throws Exception {
        Mockito.doNothing().when(mockCommentService).deleteComment(1L);

        mockMvc.perform(delete("/comments/delete-comment/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        verify(mockCommentService).deleteComment(1L);
    }

}